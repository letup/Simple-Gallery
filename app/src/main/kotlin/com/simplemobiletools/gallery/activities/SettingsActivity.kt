package com.simplemobiletools.gallery.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.simplemobiletools.commons.extensions.updateTextColors
import com.simplemobiletools.gallery.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : SimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupCustomizeColors()
        setupSameSorting()
        setupShowHiddenFolders()
        setupAutoplayVideos()
        setupShowMedia()
    }

    override fun onResume() {
        super.onResume()
        updateTextColors(settings_holder)
    }

    private fun setupCustomizeColors() {
        settings_customize_colors_holder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupSameSorting() {
        settings_same_sorting.isChecked = config.isSameSorting
        settings_same_sorting_holder.setOnClickListener {
            settings_same_sorting.toggle()
            config.isSameSorting = settings_same_sorting.isChecked
        }
    }

    private fun setupShowHiddenFolders() {
        settings_show_hidden_folders.isChecked = config.showHiddenFolders
        settings_show_hidden_folders_holder.setOnClickListener {
            settings_show_hidden_folders.toggle()
            config.showHiddenFolders = settings_show_hidden_folders.isChecked
        }
    }

    private fun setupAutoplayVideos() {
        settings_autoplay_videos_holder.setOnClickListener {
            settings_autoplay_videos.toggle()
            config.autoplayVideos = settings_autoplay_videos.isChecked
        }
    }

    private fun setupShowMedia() {
        settings_show_media.setSelection(config.showMedia)
        settings_show_media.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                config.showMedia = settings_show_media.selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}
