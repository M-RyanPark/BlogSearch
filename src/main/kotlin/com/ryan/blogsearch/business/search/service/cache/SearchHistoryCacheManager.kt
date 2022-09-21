package com.ryan.blogsearch.business.search.service.cache

import com.ryan.blogsearch.business.search.model.SearchHistory
import com.ryan.blogsearch.business.search.service.SearchHistoryDataHandler
import mu.KLogging
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 검색 이력 캐시 관리
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Service
class SearchHistoryCacheManager(
    val searchHistoryDataHandler: SearchHistoryDataHandler
) : KLogging() {

    @Cacheable(cacheNames = ["popularKeywords"], key = "'top10'")
    fun findPopularKeywordsCache(): List<SearchHistory.Response> =
        searchHistoryDataHandler.findPopularKeywords()
            .map {
                SearchHistory.Response(it.keyword, it.count)
            }

    @CachePut(cacheNames = ["popularKeywords"], key = "'top10'")
    fun refreshPopularKeywordsCache(): List<SearchHistory.Response> {
        logger.info { "popularKeywords refreshed" }
        return searchHistoryDataHandler.findPopularKeywords()
            .map {
                SearchHistory.Response(it.keyword, it.count)
            }
    }

}
