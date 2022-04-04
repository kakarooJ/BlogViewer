package com.kakaroo.blogviewer.utility

object Common {
    const val MY_TAG                  =   "kakaroo-blogViewer"

    const val BLOG_MAIN_URL           =   "https://kakaroo.tistory.com"
    const val SEARCH_TAG              =   "/search/"
    const val PAGE_TAG                =   "/?page="
    const val CATEGORY_TAG            =   "/category/"
    const val HREF_TAG                =   "href"
    const val SRC_TAG                 =   "src"


    //default value
    /*
    const val BLOG_MAIN_URL           =   "https://youngest-programming.tistory.com"
    const val SELECTOR_ARTICLE_SYNTAX     =   "div.list_content"//<article ....  /article>
    const val SELECTOR_CATEGORY_SYNTAX    =   "a.link_cate"   //<a href="/category/Kotlin" class="link-category">Kotlin</a>
    const val SELECTOR_ARTICLE_URL_SYNTAX =   "a.link_post"    //<a href="/69" class="link-article">, attr="href"
    const val SELECTOR_TITLE_SYNTAX       =   ".tit_post"
    const val SELECTOR_DATE_SYNTAX       =   ".txt_date"
    const val SELECTOR_IMAGE_SYNTAX       =   "img"//<img src="https://...png" class="img-thumbnail", attr="src"
    const val SELECTOR_SUMMARY_SYNTAX     =   "p.txt_post"  //<p class="summary">*/

    const val SELECTOR_ARTICLE_SYNTAX     =   "article"//<article ....  /article>
    const val SELECTOR_CATEGORY_SYNTAX    =   "a.link-category"   //<a href="/category/Kotlin" class="link-category">Kotlin</a>
    const val SELECTOR_ARTICLE_URL_SYNTAX =   "a.link-article"    //<a href="/69" class="link-article">
    const val SELECTOR_TITLE_SYNTAX       =   ".title"
    const val SELECTOR_DATE_SYNTAX       =   ".date"
    const val SELECTOR_IMAGE_SYNTAX       =   "img.img-thumbnail"//"img[src\$=.png]"  //<img src="https://...png" class="img-thumbnail"
    const val SELECTOR_SUMMARY_SYNTAX     =   ".summary"  //<p class="summary">

    const val COROUTINE_THREAD_POOL_NUM     = 8

    const val ARTICLE_URL       = 0

    const val REQUEST_INTERNET_PERMISSION   =   10

    const val HTTP_CRAWLING_TIMEOUT_MILLIS  =   10000L

    //const val SUBJECT_VIEW = 1
    //const val ARTICLE_VIEW = 2

    const val MAX_PAGE_NUM      =       50

    enum class EditorInputType {
        NONE,
        PAGE,
        SEARCH
    }

    const val PREF_KEY_INPUT_URL    =   "url_input_key"
    const val PREF_KEY_ARTICLE_TAG    =   "article_tag_key"
    const val PREF_KEY_CATEGORY_TAG    =   "category_tag_key"
    const val PREF_KEY_ARTICLE_LINK_TAG    =   "articleLink_tag_key"
    const val PREF_KEY_TITLE_TAG    =   "title_tag_key"
    const val PREF_KEY_DATE_TAG    =   "date_tag_key"
    const val PREF_KEY_IMAGE_TAG    =   "image_tag_key"
    const val PREF_KEY_SUMMARY_TAG    =   "summary_tag_key"
    const val PREF_KEY_PAGE_MAX_NUM    =   "page_max_num_key"

}