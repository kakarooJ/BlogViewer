package com.kakaroo.blogviewer

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import com.kakaroo.blogviewer.utility.Common

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)


            setEditTextPreference("url_input_key", Common.BLOG_MAIN_URL)
            setEditTextPreference("article_tag_key", Common.SELECTOR_ARTICLE_SYNTAX)
            setEditTextPreference("category_tag_key", Common.SELECTOR_CATEGORY_SYNTAX)
            setEditTextPreference("articleLink_tag_key", Common.SELECTOR_ARTICLUEURL_SYNTAX)
            setEditTextPreference("title_tag_key", Common.SELECTOR_TITLE_SYNTAX)
            setEditTextPreference("date_tag_key", Common.SELECTOR_DATE_SYNTAX)
            setEditTextPreference("image_tag_key", Common.SELECTOR_IMAGE_SYNTAX)
            setEditTextPreference("summary_tag_key", Common.SELECTOR_SUMMARY_SYNTAX)
            setEditTextPreference("page_max_num_key", Common.MAX_PAGE_NUM.toString())
        }

        private fun setEditTextPreference(keyValue: String, substituted: String) {
            val editTextPref: EditTextPreference? = findPreference(keyValue)
            editTextPref?.summary = if(editTextPref?.text == null || editTextPref?.text == "") {
                val pref = PreferenceManager.getDefaultSharedPreferences(this.context)
                pref.edit().putString(keyValue, substituted).apply()
                substituted
            } else editTextPref?.text

            editTextPref?.setOnPreferenceChangeListener{ preference, newValue ->
                if(newValue == null || newValue == "") {
                    //val pref = PreferenceManager.getDefaultSharedPreferences(this.context)
                    //pref.edit().putString(keyValue, substituted).apply()
                    Log.i(Common.MY_TAG, "키워드 입력값이 없습니다.")
                    //preference.summary = substituted
                    false
                } else {
                    preference.summary = newValue.toString()
                }
                true
            }
        }

    }
}