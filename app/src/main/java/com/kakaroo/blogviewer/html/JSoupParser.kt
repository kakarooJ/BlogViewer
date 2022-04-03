package com.kakaroo.blogviewer.html

import android.os.AsyncTask
import android.util.Log
import com.kakaroo.blogviewer.utility.Common
import com.kakaroo.blogviewer.MainActivity
import com.kakaroo.blogviewer.entity.Article
import com.kakaroo.blogviewer.entity.ArticlesTag
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

//TODO : Coroutine
class JSoupParser(
    private val url: String,
    private val uriTag: String,
    private val articlesTag: ArticlesTag,
    private val callback: MainActivity.onPostExecuteListener): AsyncTask<Void, Void, Void>() {

    var mArticleList = ArrayList<Article>()
    //var mCategorySet = mutableSetOf<String>()

    var bCatched = false

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        try {
            val doc: Document = Jsoup.connect(url+uriTag)
                .ignoreContentType(true)
                .get()

            //Log.i(Common.MY_TAG, doc.toString())

            val contentElements: Elements = doc.select(articlesTag.articleTag)
            for ((i, elem) in contentElements.withIndex()) {
                val category = elem.select(articlesTag.categoryTag).text()
                val articleUrl = elem.select(articlesTag.articleUrlTag).first().attr(Common.HREF_TAG)
                val title = elem.select(articlesTag.titleTag).text()
                val date = elem.select(articlesTag.dateTag).text()
                val categoryUrl = elem.select(articlesTag.categoryTag).first().attr(Common.HREF_TAG)
                val imageUrl = elem.select(articlesTag.imageTag).attr(Common.SRC_TAG)
                val summary = elem.select(articlesTag.summaryTag).text()

                //mCategorySet.add(category)
                mArticleList.add(Article(i, category, title, date, url+categoryUrl, url+articleUrl, imageUrl, summary))
            }

        } catch (e: IOException) {
            // HttpUrlConnection will throw an IOException if any 4XX
            // response is sent. If we request the status again, this
            // time the internal status will be properly set, and we'll be
            // able to retrieve it.
            Log.e(Common.MY_TAG, "Jsoup connection has error: $e")
            bCatched = true
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        callback.onPostExecute(mArticleList, /*mCategorySet,*/ bCatched)
    }
}