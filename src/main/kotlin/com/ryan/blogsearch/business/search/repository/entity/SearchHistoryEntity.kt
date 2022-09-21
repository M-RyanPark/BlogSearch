package com.ryan.blogsearch.business.search.repository.entity

import com.ryan.blogsearch.business.common.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Entity
@Table(
    name = "search_history",
    indexes = [Index(name = "search_count", columnList = "count")]
)
class SearchHistoryEntity(
    @Id
    @Column(length = 100, nullable = false)
    var keyword: String,
    @Column(nullable = false)
    var count: Int,
) : BaseEntity() {
    override fun identifier(): Any = keyword
}
