package com.ryan.blogsearch.business.search.service.cache

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * 검색 이력 Cache 갱신을 담당하는 scheduler
 * 5초 마다 검색 db에서 데이터를 읽어와 캐시를 갱신한다
 * 블로그 인기 검색어는 5초 단위로 갱신해도 사용성에 문제가 없다고 판단하여 성능에 중점을 두고 설정
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Service
class SearchHistoryCacheScheduler(
    val searchHistoryCacheManager: SearchHistoryCacheManager
) {
    @Scheduled(fixedRate = 5000, initialDelay = 10000)
    fun refreshSearchHistoryCacheEvery5Second() {
        searchHistoryCacheManager.refreshPopularKeywordsCache()
    }
}

