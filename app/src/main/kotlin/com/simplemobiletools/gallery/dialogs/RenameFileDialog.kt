package com.simplemobiletools.gallery.dialogs

import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.simplemobiletools.filepicker.extensions.*
import com.simplemobiletools.gallery.R
import com.simplemobiletools.gallery.activities.SimpleActivity
import com.simplemobiletools.gallery.helpers.Config
import kotlinx.android.synthetic.main.rename_file.view.*
import java.io.File

class RenameFileDialog(val activity: SimpleActivity, val file: File, val callback: (newFile: File) -> Unit) {

    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.rename_file, null) as ViewGroup
        val fullName = file.name
        val dotAt = fullName.lastIndexOf(".")
        var name = fullName

        if (dotAt > 0) {
            name = fullName.substring(0, dotAt)
            val extension = fullName.substring(dotAt + 1)
            view.file_extension.setText(extension)
        }

        view.file_name.setText(name)
        view.file_path.text = "${activity.humanizePath(file.parent)}/"

        AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create().apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            activity.setupDialogStuff(view, this, R.string.rename_file)
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
                val filename = view.file_name.value
                val extension = view.file_extension.value

                if (filename.isEmpty()) {
                    context.toast(R.string.filename_cannot_be_empty)
                    return@setOnClickListener
                }

                if (extension.isEmpty()) {
                    context.toast(R.string.extension_cannot_be_empty)
                    return@setOnClickListener
                }

                val newFile = File(file.parent, "$filename.$extension")
                if (!newFile.name.isAValidFilename()) {
                    context.toast(R.string.invalid_name)
                    return@setOnClickListener
                }

                if (context.needsStupidWritePermissions(file.absolutePath)) {
                    if (activity.isShowingPermDialog(file))
                        return@setOnClickListener

                    val document = context.getFileDocument(file.absolutePath, Config.newInstance(context).treeUri)
                    if (document.canWrite())
                        document.renameTo(newFile.name)
                    sendSuccess(file, newFile)
                    dismiss()
                } else if (file.renameTo(newFile)) {
                    sendSuccess(file, newFile)
                    dismiss()
                } else {
                    context.toast(R.string.rename_file_error)
                }
            })
        }
    }

    private fun sendSuccess(oldFile: File, newFile: File) {
        if (activity.updateInMediaStore(oldFile, newFile)) {
            callback.invoke(newFile)
        } else {
            val changedFiles = arrayListOf(oldFile, newFile)
            activity.scanFiles(changedFiles) {
                callback.invoke(newFile)
            }
        }
    }
}
