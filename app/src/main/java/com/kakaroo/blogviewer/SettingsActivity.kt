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


            setEditTextPreference(Common.PREF_KEY_INPUT_URL, Common.BLOG_MAIN_URL)
            setEditTextPreference(Common.PREF_KEY_ARTICLE_TAG, Common.SELECTOR_ARTICLE_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_CATEGORY_TAG, Common.SELECTOR_CATEGORY_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_ARTICLE_LINK_TAG, Common.SELECTOR_ARTICLE_URL_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_TITLE_TAG, Common.SELECTOR_TITLE_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_DATE_TAG, Common.SELECTOR_DATE_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_IMAGE_TAG, Common.SELECTOR_IMAGE_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_SUMMARY_TAG, Common.SELECTOR_SUMMARY_SYNTAX)
            setEditTextPreference(Common.PREF_KEY_PAGE_MAX_NUM, Common.MAX_PAGE_NUM.toString())
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