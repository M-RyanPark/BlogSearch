/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.error

import com.ryan.blogsearch.infrastructure.error.exception.DefinedException
import com.ryan.blogsearch.infrastructure.error.model.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.util.WebUtils

/**
 * [org.springframework.stereotype.Controller] 에서 발생하는 [Exception]에 대한 처리를 위한 [ControllerAdvice].
 * [handleExceptionInternal]을 override 하고, [ExceptionAttribute]을 생성하여 [ApiErrorResponse]로 변환하여 응답한다.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
@ControllerAdvice
class GlobalExceptionAdvice : ResponseEntityExceptionHandler() {
    override fun handleExceptionInternal(
        exception: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        if (status.is5xxServerError) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception, WebRequest.SCOPE_REQUEST)

            exception
                .takeUnless { it is DefinedException }
                ?.run {
                    logUnexpectedServerError(this)
                }
        }

        return ExceptionAttribute.make(exception, status).toResponseEntity()
    }

    @ExceptionHandler(DefinedException::class)
    fun exceptionHandler(ex: DefinedException, request: WebRequest): ResponseEntity<Any> =
        this.handleExceptionInternal(ex, null, HttpHeaders(), ex.exceptionAttribute.status, request)

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(ex: Exception, request: WebRequest): ResponseEntity<Any> =
        this.handleExceptionInternal(ex, null, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)

    fun logUnexpectedServerError(exception: Exception) {
        logger.error("Unexpected Internal Server Error", exception)
    }
}
