package com.kakaroo.blogviewer.entity

data class Article(val idx: Int, val categoryName: String, var title: String, var date: String,
                   val categoryUrl: String, val articleUrl: String, val imgUrl: String, var summary: String)