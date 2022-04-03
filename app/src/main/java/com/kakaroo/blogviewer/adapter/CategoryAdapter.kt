package com.kakaroo.blogviewer.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakaroo.blogviewer.entity.Category
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaroo.blogviewer.utility.Common
import com.kakaroo.blogviewer.R

class CategoryAdapter(private val context: Context, private val listData: ArrayList<Category>?)
    : RecyclerView.Adapter<CategoryAdapter.TopicHolder>() {

    private val mContext: Context = context
    private var mCategoryList = listData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_recycler,parent,false)

        return TopicHolder(view).apply {}
    }

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        val category: Category? = listData?.get(position)
        if (category != null) {
            holder.setItem(category)
        }
        holder.rv_article.adapter = ArticleAdapter(context, mCategoryList?.get(position)?.articles)
        holder.rv_article.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.rv_article.setHasFixedSize(true)
        //holder.tv_topic.text = mCategoryList?.get(position)?.title
        holder.tv_topicNum.text = mCategoryList?.get(position)?.articles?.size.toString() + " 개"
        //holder.tv_stockPrice.text = mCategoryList?.get(position)?.price
    }

    override fun getItemCount(): Int = listData?.size ?: 0

    inner class TopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_category: TextView = itemView.findViewById(R.id.tv_category)
        val tv_topicNum: TextView = itemView.findViewById(R.id.tv_topicNum)
        val rv_article: RecyclerView = itemView.findViewById(R.id.rv_articles)

        fun setItem(category: Category) {
            tv_category.text = category.name

            // 아이템 클릭 이벤트 처리.
            val url = /*Common.MAIN_URL_PREF + */category.articles[0].categoryUrl
            tv_category.setOnClickListener(View.OnClickListener() {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                mContext.startActivity(intent)
            })

        }
    }

}