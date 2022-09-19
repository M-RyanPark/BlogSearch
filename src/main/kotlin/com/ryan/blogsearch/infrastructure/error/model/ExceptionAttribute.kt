/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.error.model

import com.ryan.blogsearch.infrastructure.error.exception.DefinedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Exception이 발생할 경우 사용할 응답 규격
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
interface ExceptionAttribute {
    val code: String
    val status: HttpStatus
    val message: String

    fun toResponseEntity(): ResponseEntity<Any> = ResponseEntity(ApiErrorResponse(this.code, this.message), this.status)

    companion object {
        fun make(throwable: Throwable?, status: HttpStatus): ExceptionAttribute {
            return when (throwable) {
                is DefinedException -> throwable.exceptionAttribute
                else -> status.let {
                    it.toDefinedExceptionAttribute()
                        ?: CustomizableExceptionAttribute(
                            it,
                            throwable?.message ?: DefinedExceptionAttribute.INTERNAL_SERVER_ERROR.message
                        )
                }

            }
        }
    }
}
