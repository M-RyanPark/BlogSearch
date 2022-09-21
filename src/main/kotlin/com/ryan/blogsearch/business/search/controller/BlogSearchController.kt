package com.ryan.blogsearch.business.search.controller

import com.ryan.blogsearch.business.search.model.SearchBlog
import com.ryan.blogsearch.business.search.service.IntegrationBlogApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@RestController
@RequestMapping("blog/search")
class BlogSearchController(
    val integrationBlogApi: IntegrationBlogApi
) {
    @GetMapping
    fun get(@ModelAttribute @Valid request: SearchBlog.Request): SearchBlog.Response =
        integrationBlogApi.search(request)

}
