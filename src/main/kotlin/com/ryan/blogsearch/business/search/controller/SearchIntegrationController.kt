package com.ryan.blogsearch.business.search.controller

import com.ryan.blogsearch.business.search.model.SearchBlog
import com.ryan.blogsearch.business.search.model.SearchHistory
import com.ryan.blogsearch.business.search.service.SearchIntegrationApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * 통합 검색 controller
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@RestController
@RequestMapping("blog/search")
class SearchIntegrationController(
    val searchIntegrationApi: SearchIntegrationApi
) {
    @GetMapping
    fun search(@ModelAttribute @Valid request: SearchBlog.Request): SearchBlog.Response =
        searchIntegrationApi.search(request)

    @GetMapping("popular-keyword")
    fun popularKeyword(): List<SearchHistory.Response> =
        searchIntegrationApi.findPopularKeywords()
}
