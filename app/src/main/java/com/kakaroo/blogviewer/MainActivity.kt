package com.kakaroo.blogviewer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakaroo.blogviewer.adapter.CategoryAdapter
import com.kakaroo.blogviewer.databinding.ActivityMainBinding
import com.kakaroo.blogviewer.entity.Article
import com.kakaroo.blogviewer.entity.ArticlesTag
import com.kakaroo.blogviewer.entity.Category
import com.kakaroo.blogviewer.html.JSoupParser
import com.kakaroo.blogviewer.utility.Common
import com.kakaroo.blogviewer.utility.MyUtility

class MainActivity : AppCompatActivity() {

    init{
        instance = this
    }
    companion object{
        private var instance:MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }

    //전역변수로 binding 객체선언
    private var mBinding: ActivityMainBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    var mCategoryList: ArrayList<Category> = ArrayList<Category>()
    var mArticleList: ArrayList<Article> = ArrayList<Article>()

    var mAdapter = CategoryAdapter(this, mCategoryList)
    lateinit var mRecyclerView: RecyclerView
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mRecyclerView = binding.recyclerView
                // use a linear layout manager
                mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = mAdapter

        mPref = PreferenceManager.getDefaultSharedPreferences(this.applicationContext)

        checkInternetPermissions()

        registerListener()

        // editText에서 완료 클릭 시
        binding.etKeyword.setOnEditorActionListener  { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.btSearch.performClick()
                handled = true
            }
            handled
        }
    }

    override fun onDestroy() {
        Log.e(Common.MY_TAG, "onDestroy called")
        super.onDestroy()
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val nextIntent = Intent(this, SettingsActivity::class.java)
                startActivity(nextIntent)
            }
            R.id.reset_list -> {
                mCategoryList.clear()
                mAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerListener() {
        binding.btSearch.setOnClickListener(ButtonListener())
    }

    inner class ButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            if (v != null) {
                when(v.id) {
                    R.id.bt_search -> {
                        if(!MyUtility().isNetworkConnected(applicationContext)) {
                            Log.i(Common.MY_TAG, "인터넷이 연결되어 있지 않습니다.")
                            Toast.makeText(applicationContext, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                            return
                        }

                        mCategoryList.clear()  //새로 갱신
                        mArticleList.clear()

                        //검색결과 구하기
                        var searchType = Common.EditorInputType.NONE
                        val searchStr = binding.etKeyword.text.toString()
                        if(binding.etKeyword.text.isNotEmpty()) {
                            searchType = if(isNumber(searchStr)) Common.EditorInputType.PAGE else Common.EditorInputType.SEARCH
                        }

                        hideKeyboard()  //키보드를 내린다.
                        //binding.etKeyword.setText("")   //Editor를 지운다.

                        val articleTag = mPref.getString("article_tag_key", Common.SELECTOR_ARTICLE_SYNTAX) ?: Common.SELECTOR_ARTICLE_SYNTAX
                        val categoryTag = mPref.getString("category_tag_key", Common.SELECTOR_CATEGORY_SYNTAX) ?: Common.SELECTOR_CATEGORY_SYNTAX
                        val articleUrlTag = mPref.getString("articleLink_tag_key", Common.SELECTOR_ARTICLUEURL_SYNTAX) ?: Common.SELECTOR_ARTICLUEURL_SYNTAX
                        val titleTag = mPref.getString("title_tag_key", Common.SELECTOR_TITLE_SYNTAX) ?: Common.SELECTOR_TITLE_SYNTAX
                        val dateTag = mPref.getString("date_tag_key", Common.SELECTOR_DATE_SYNTAX) ?: Common.SELECTOR_DATE_SYNTAX
                        val imageTag = mPref.getString("image_tag_key", Common.SELECTOR_IMAGE_SYNTAX) ?: Common.SELECTOR_IMAGE_SYNTAX
                        val summaryTag = mPref.getString("summary_tag_key", Common.SELECTOR_SUMMARY_SYNTAX) ?: Common.SELECTOR_SUMMARY_SYNTAX
                        val pageMaxNum = mPref.getString("page_max_num_key", Common.MAX_PAGE_NUM.toString()) ?: Common.MAX_PAGE_NUM.toString()
                        val pageMaxNumInt = pageMaxNum.toInt()

                        val articlesTag = ArticlesTag(articleTag, categoryTag, articleUrlTag, titleTag, dateTag, imageTag, summaryTag)

                        val url = mPref.getString("blog_url_key", Common.BLOG_MAIN_URL) ?: Common.BLOG_MAIN_URL

                        var bComplete = if(searchType == Common.EditorInputType.NONE) false else true
                        var pageIndex = 0
                        var asyncTryCnt = 0

                        do {
                            var uriTag = ""
                            when(searchType) {
                                Common.EditorInputType.PAGE -> uriTag += Common.PAGE_TAG + searchStr
                                Common.EditorInputType.SEARCH -> uriTag += Common.SEARCH_TAG + searchStr
                                else -> uriTag += Common.PAGE_TAG + ++pageIndex
                            }

//                            if(pageIndex > 1) {
//                                Thread.sleep(100)
//                            }

                            asyncTryCnt++
                            //Log.d(Common.MY_TAG, "pre: asyncTryCnt[$asyncTryCnt]")
                            binding.btSearch.isEnabled = false

                            CoroutineScope()

                            val jsoupAsyncTask =
                                JSoupParser(url, uriTag, articlesTag, object : onPostExecuteListener {
                                    override fun onPostExecute(
                                        result: ArrayList<Article>,
                                        /*categorySet: MutableSet<String>,*/
                                        bError: Boolean
                                    ) {
                                        asyncTryCnt--
                                        if(bError || result.size == 0) {
                                            Log.e(Common.MY_TAG, "asyncTryCnt[$asyncTryCnt], pageIndex[$pageIndex], error[$bError}, result size is ${result.size}")
                                            bComplete = true
                                        } else {
                                            mArticleList.addAll(result)
                                            Log.i(Common.MY_TAG, "asyncTryCnt[$asyncTryCnt], url[$url] - result[${result.size}]")

                                            if(searchType == Common.EditorInputType.NONE)
                                                mCategoryList.clear()

                                            val mapList = mArticleList.groupBy { it.categoryName }
                                            //Log.i(Common.MY_TAG, mapList.toString())

                                            mapList.forEach{ item -> mCategoryList.add(Category(item.key,
                                                item.value as ArrayList<Article>
                                            ))}
                                            mAdapter.notifyDataSetChanged()
                                        }
                                        if(asyncTryCnt == 0) {
                                            Log.d(Common.MY_TAG, "btSearch is enabled")
                                            binding.btSearch.isEnabled = true
                                        }
                                        
                                        runOnUiThread {
                                            if (result.size == 0 && mCategoryList.isEmpty()) {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "게시글이 없습니다.!!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                bComplete = true
                                            }
                                        }
                                    }
                                })
                            jsoupAsyncTask.execute()
                        } while(!bComplete && pageIndex < pageMaxNumInt)
                    }
                }
            }
        }
    }

    private fun printList() {
        if(mCategoryList.size != 0) {
            Log.i(Common.MY_TAG, mCategoryList.toString())
        }
    }

    /*private fun updateTextView(key: String, size: Int) {
        binding.tvResult.text = "${key}에 관련된 ${size}개의 기사를 찾았습니다."
    }*/

    private fun isNumber(str: String) = !(str.toIntOrNull() == null)

    private fun checkInternetPermissions() {
        val permissionResult = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
        if(permissionResult != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "인터넷 권한이 없습니다.!!", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.INTERNET), Common.REQUEST_INTERNET_PERMISSION)
        }
    }
    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === Common.REQUEST_INTERNET_PERMISSION) {
            if (grantResults.size > 0) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED)
                        System.exit(0)
                }
            }
        }
    }

    interface onPostExecuteListener {
        fun onPostExecute(result: ArrayList<Article>, /*categorySet: MutableSet<String>,*/ bError: Boolean)
    }

}
