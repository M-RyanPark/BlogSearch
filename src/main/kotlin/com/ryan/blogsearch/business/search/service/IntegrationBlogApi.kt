package com.ryan.blogsearch.business.search.service

import com.ryan.blogsearch.business.search.client.kakao.KakaoBLogClient
import com.ryan.blogsearch.business.search.client.kakao.KakaoBlogToSearchBlogResponse
import com.ryan.blogsearch.business.search.client.kakao.KakaoApiException
import com.ryan.blogsearch.business.search.client.kakao.SearchBlogToKakaoBlogRequestModel
import com.ryan.blogsearch.business.search.model.SearchBlog
import com.ryan.blogsearch.infrastructure.error.exception.BusinessException
import com.ryan.blogsearch.infrastructure.error.exception.InternalServerException
import org.springframework.stereotype.Service

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Service
class IntegrationBlogApi(
    val kakaoBLogClient: KakaoBLogClient
) {
    fun search(request: SearchBlog.Request): SearchBlog.Response =
        try {
            request.toClientRequest { SearchBlogToKakaoBlogRequestModel }
                .invoke(request)
                .let { kakaoBLogClient.searchBLog(it) }
                .let { KakaoBlogToSearchBlogResponse.invoke(request, it) }
        } catch (ke: KakaoApiException) {
            throw BusinessException(ke.error.let { "${it.errorType} / ${it.message}" })
        } catch (e: Exception) {
            throw InternalServerException()
        }
}
