package com.kakaroo.blogviewer.utility

object Common {
    const val MY_TAG                  =   "kakaroo-blogViewer"

    const val BLOG_MAIN_URL           =   "https://kakaroo.tistory.com"
    const val SEARCH_TAG              =   "/search/"
    const val PAGE_TAG                =   "/?page="
    const val CATEGORY_TAG            =   "/category/"
    const val HREF_TAG                =   "href"
    const val SRC_TAG                 =   "src"

    const val SELECTOR_ARTICLE_SYNTAX     =   "article"//<article ....  /article>
    const val SELECTOR_CATEGORY_SYNTAX    =   "a.link-category"   //<a href="/category/Kotlin" class="link-category">Kotlin</a>
    const val SELECTOR_ARTICLUEURL_SYNTAX =   "a.link-article"    //<a href="/69" class="link-article">
    const val SELECTOR_TITLE_SYNTAX       =   ".title"
    const val SELECTOR_DATE_SYNTAX       =   ".date"
    const val SELECTOR_IMAGE_SYNTAX       =   "img.img-thumbnail"//"img[src\$=.png]"  //<img src="https://...png" class="img-thumbnail"
    const val SELECTOR_SUMMARY_SYNTAX     =   ".summary"  //<p class="summary">

    const val ARTICLE_URL       = 0

    val REQUEST_INTERNET_PERMISSION     =   10

    //const val SUBJECT_VIEW = 1
    //const val ARTICLE_VIEW = 2

    const val MAX_PAGE_NUM      =       50

    enum class EditorInputType {
        NONE,
        PAGE,
        SEARCH
    }

}