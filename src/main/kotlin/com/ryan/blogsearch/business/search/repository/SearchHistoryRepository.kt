package com.ryan.blogsearch.business.search.repository

import com.ryan.blogsearch.business.search.repository.entity.SearchHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
interface SearchHistoryRepository : JpaRepository<SearchHistoryEntity, String> {
    fun findTop10ByOrderByCountDesc(): List<SearchHistoryEntity>
}
