/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.business.common

import com.ryan.blogsearch.business.common.EntityCommon
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

/**
 * [EntityCommon] equals 에 대한 테스트
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
class EntityCommonTest {

    @Test
    fun `EntityCommon equals test`() {
        val a11 = AEntity(1)
        val a12 = AEntity(1)
        val a2 = AEntity(2)
        val b1 = BEntity(1)
        val c1 = CEntity("1")
        val a1n: EntityCommon? = null
        val notEntity = NotEntity(1)


        assertEquals(a11, a11, "Same instance validation fail")
        assertEquals(a11, a12, "Same entity and different instance validation fail")
        assertNotEquals(a11, a1n, "null validation fail")
        assertNotEquals(a11, a2, "different identifier validation fail")
        assertNotEquals(a11, b1, "different class and same identifier validation fail")
        assertNotEquals(a11, c1, "different class and identifier validation fail")
        assertNotEquals(a11, notEntity, "not entity class validation fail")
    }

    class AEntity(private val id: Int) : EntityCommon() {
        override fun identifier(): Any {
            return id
        }
    }

    class BEntity(private val id: Int) : EntityCommon() {
        override fun identifier(): Any {
            return id
        }
    }

    class CEntity(private val id: String) : EntityCommon() {
        override fun identifier(): Any {
            return id
        }
    }

    class NotEntity(private val id: Int)
}
