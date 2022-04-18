package com.kakaroo.blogviewer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kakaroo.blogviewer.entity.Article

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kakaroo.blogviewer.R

class ArticleAdapter(private val context: Context, private val listData: ArrayList<Article>?)
    : RecyclerView.Adapter<ArticleAdapter.CustomViewHolder>() {

    private val mContext: Context = context
    private var mArticleList: ArrayList<Article>? = listData
    //private inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item_cardview, parent, false)

        return CustomViewHolder(view).apply {}
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val article: Article? = listData?.get(position)
        if (article != null) {
            holder.setItem(article)
        }
    }

    override fun getItemCount(): Int = listData?.size ?: 0

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgThumb: ImageView = itemView.findViewById(R.id.iv_thumb)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvSummary: TextView = itemView.findViewById(R.id.tv_summary)

        fun setItem(article: Article) {
            val imgName = if(article.imgUrl == "") R.drawable.img_thumb else article.imgUrl

            Glide
                .with(itemView.context)
                .load(imgName)   //img drawable
                //.centerCrop()
                .transform(CenterCrop(), RoundedCorners(30))
                .placeholder(android.R.drawable.stat_sys_upload)
                .into(imgThumb)
            tvTitle.text = article.title.toString()
            tvDate.text = article.date.toString()
            tvSummary.text = article.summary.toString()

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(View.OnClickListener() {
                val url = /*Common.MAIN_URL_PREF + */article.articleUrl
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.articleUrl))
                mContext.startActivity(intent)
            })
        }
    }
}