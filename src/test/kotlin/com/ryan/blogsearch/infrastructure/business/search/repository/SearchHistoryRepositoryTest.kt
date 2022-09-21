package com.ryan.blogsearch.infrastructure.business.search.repository

import com.ryan.blogsearch.business.search.repository.SearchHistoryRepository
import com.ryan.blogsearch.business.search.repository.entity.SearchHistoryEntity
import com.ryan.blogsearch.infrastructure.common.JpaTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.util.AssertionErrors.assertEquals

/**
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2022-09-21
 */
class SearchHistoryRepositoryTest : JpaTest() {

    @Autowired
    lateinit var searchHistoryRepository: SearchHistoryRepository

    @Test
    fun `create history`() {
        val targetEntity = SearchHistoryEntity("test", 1)

        val saved = searchHistoryRepository.save(targetEntity)

        assertEquals("keyword", targetEntity.keyword, saved.keyword)
        assertEquals("count", targetEntity.count, saved.count)
    }

    @Test
    fun `count top 10`() {
        for (i in 1..10) {
            searchHistoryRepository.save(SearchHistoryEntity("test$i", i))
        }

        val historyTop1 = searchHistoryRepository.findTop10ByOrderByCountDesc()[0]

        assertEquals("top1keyword", historyTop1.keyword, "test10")
        assertEquals("top1count", historyTop1.count, 10)
    }
}
