/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.business.common

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

/**
 * table 에서 공통으로 사용하는 생성일시, 수정일시를 규격화한 abstract class
 *
 * 생성 일시 [createDateTime], 최종 수정 일시 [updateDateTime] 값이 자동으로 입력되도록 [CreatedDate], [LastModifiedDate] 을 설정하고
 * [com.ryan.blogsearch.infrastructure.jpa.JpaConfig] 에 [org.springframework.data.jpa.repository.config.EnableJpaAuditing]을 설정한다.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : EntityCommon() {
    @JsonIgnore
    @Column(name = "create_datetime")
    @CreatedDate
    lateinit var createDateTime: LocalDateTime

    @JsonIgnore
    @Column(name = "update_datetime")
    @LastModifiedDate
    lateinit var updateDateTime: LocalDateTime
}
