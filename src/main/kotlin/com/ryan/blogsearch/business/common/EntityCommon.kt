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
import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * Jpa Entity 구분을 위해 반드시 필요한 equals(), hasCode()공통으로 정의한 abstract class
 * [identifier] 를 추상 function 으로 정의하고 이 값을 기준으로 [equals], [hashCode]를 공통화 한다
 *
 * Entity class 는 data 지시자를 사용할 경우 [hashCode], [toString] 등에서 문제가 발생할 수 있으므로
 * data 지시자를 사용하지 않고 [EntityCommon]을 상속받아서 구현하도록 한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@MappedSuperclass
abstract class EntityCommon : Serializable {

    @JsonIgnore
    abstract fun identifier(): Any

    override fun equals(other: Any?): Boolean =
        other
            ?.let {
                when {
                    it === this -> true
                    it::class != this::class -> false
                    it is EntityCommon -> it.identifier() == this.identifier()
                    else -> false
                }
            } ?: false

    override fun hashCode(): Int {
        return identifier().hashCode()
    }
}
