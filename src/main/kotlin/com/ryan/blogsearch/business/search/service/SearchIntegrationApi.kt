package com.ryan.blogsearch.business.search.service

import com.ryan.blogsearch.business.search.client.kakao.KakaoBLogClient
import com.ryan.blogsearch.business.search.client.kakao.KakaoBlogToSearchBlogResponse
import com.ryan.blogsearch.business.search.client.kakao.KakaoApiException
import com.ryan.blogsearch.business.search.client.kakao.SearchBlogToKakaoBlogRequestModel
import com.ryan.blogsearch.business.search.model.SearchBlog
import com.ryan.blogsearch.business.search.model.SearchHistory
import com.ryan.blogsearch.business.search.service.cache.SearchHistoryCacheManager
import com.ryan.blogsearch.business.search.service.event.SearchHistoryRecordEvent
import com.ryan.blogsearch.infrastructure.error.exception.BusinessException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

/**
 * 검색 통합 api 서비스
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Service
class SearchIntegrationApi(
    val kakaoBLogClient: KakaoBLogClient,
    val searchHistoryCacheManager: SearchHistoryCacheManager,
    val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun search(request: SearchBlog.Request): SearchBlog.Response =
        try {
            request.toClientRequest { SearchBlogToKakaoBlogRequestModel }
                .invoke(request)
                .let { kakaoBLogClient.searchBLog(it) }
                .let { KakaoBlogToSearchBlogResponse.invoke(request, it) }
                .also {
                    applicationEventPublisher.publishEvent(SearchHistoryRecordEvent(it.meta.keyword))
                }
        } catch (ke: KakaoApiException) {
            throw BusinessException(ke.error.let { "${it.errorType} / ${it.message}" })
        } catch (e: Exception) {
            throw e
        }

    fun findPopularKeywords(): List<SearchHistory.Response> =
        searchHistoryCacheManager.findPopularKeywordsCache()
}
