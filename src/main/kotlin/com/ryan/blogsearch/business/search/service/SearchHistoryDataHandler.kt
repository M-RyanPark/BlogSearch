package com.ryan.blogsearch.business.search.service

import com.ryan.blogsearch.business.search.repository.SearchHistoryRepository
import com.ryan.blogsearch.business.search.repository.entity.SearchHistoryEntity
import com.ryan.blogsearch.business.search.service.event.SearchHistoryRecordEvent
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 검색 기록 데이터 관리
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Service
@Transactional
class SearchHistoryDataHandler(
    val searchHistoryRepository: SearchHistoryRepository
) {

    fun findPopularKeywords(): List<SearchHistoryEntity> =
        searchHistoryRepository.findTop10ByOrderByCountDesc()

    @Async
    @EventListener(SearchHistoryRecordEvent::class)
    fun recordSearchHistory(event: SearchHistoryRecordEvent) {
        searchHistoryRepository.findByIdOrNull(event.keyword)
            ?.run { this.count++ }
            ?: createHistory(event.keyword)
    }

    fun createHistory(keyword: String): SearchHistoryEntity =
        searchHistoryRepository.save(SearchHistoryEntity(keyword, 1))
}
