package com.kakaroo.blogviewer

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.kakaroo.blogviewer.utility.Common

class SettingsActivity : AppCompatActivity() {

    lateinit var mDialog : Dialog

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

        configureDialog()
    }

    private fun configureDialog() {
        mDialog = Dialog(this)
        mDialog.setContentView(R.layout.custom_dialog)

        val window: Window? = mDialog.window
        if (window != null) {
            // 백그라운드 투명
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params: WindowManager.LayoutParams = window.attributes
            // 화면에 가득 차도록
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            //params.height = WindowManager.LayoutParams.MATCH_PARENT

            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle
            window.attributes = params
            // UI 하단 정렬
            window.setGravity(Gravity.CENTER)
        }

        //Image zoom in-out
        val imageView: SubsamplingScaleImageView = mDialog.findViewById(R.id.imgView_Hint)
        imageView.setImage(ImageSource.resource(R.drawable.settings_hint))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.btn_hint -> {
                mDialog.show()
                true
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