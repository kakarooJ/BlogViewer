package com.kakaroo.blogviewer.entity

import com.kakaroo.blogviewer.utility.Common

data class ArticlesTag(var articleTag: String = Common.SELECTOR_ARTICLE_SYNTAX,
                       var categoryTag: String = Common.SELECTOR_CATEGORY_SYNTAX,
                       var articleUrlTag: String = Common.SELECTOR_ARTICLUEURL_SYNTAX,
                       var titleTag: String = Common.SELECTOR_TITLE_SYNTAX,
                       var dateTag: String = Common.SELECTOR_DATE_SYNTAX,
                       var imageTag: String = Common.SELECTOR_IMAGE_SYNTAX,
                       var summaryTag: String = Common.SELECTOR_SUMMARY_SYNTAX) {
    init {
        if(articleTag == "") this.articleTag = Common.SELECTOR_ARTICLE_SYNTAX
        if(categoryTag == "") this.categoryTag = Common.SELECTOR_CATEGORY_SYNTAX
        if(articleUrlTag == "") this.articleUrlTag = Common.SELECTOR_ARTICLUEURL_SYNTAX
        if(titleTag == "") this.titleTag = Common.SELECTOR_TITLE_SYNTAX
        if(dateTag == "") this.dateTag = Common.SELECTOR_DATE_SYNTAX
        if(imageTag == "") this.imageTag = Common.SELECTOR_IMAGE_SYNTAX
        if(summaryTag == "") this.summaryTag = Common.SELECTOR_SUMMARY_SYNTAX
    }
}
