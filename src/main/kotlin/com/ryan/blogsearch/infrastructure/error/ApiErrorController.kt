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
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.ServletWebRequest
import javax.servlet.http.HttpServletRequest

/**
 * [AbstractErrorController]를 이용하여 customize한 [ErrorController]
 * Rest API 역할만 하는 Application이므로 어떤 media-type으로 요청 하더라도 json 규격으로 응답하므로 text/html 등의 처리는 생략한다
 *
 * [DefaultErrorAttributes.getError]를 이용하여 [Throwable]을 획득하고 이를 이용하여 [ExceptionAttribute] 및 [ExceptionAttribute]를 구성한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
@Controller
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class ApiErrorController(
    private val errorAttributes: ErrorAttributes,
    errorViewResolverList: List<ErrorViewResolver>,
) : AbstractErrorController(errorAttributes, errorViewResolverList) {

    @RequestMapping
    fun error(request: HttpServletRequest): ResponseEntity<Any> =
        makeExceptionAttribute(request).let {
            when (it.status) {
                HttpStatus.NO_CONTENT -> ResponseEntity(it.status)
                else -> it.toResponseEntity()
            }
        }

    fun makeExceptionAttribute(request: HttpServletRequest): ExceptionAttribute =
        when (val exception = this.errorAttributes.getError(ServletWebRequest(request))) {
            is DefinedException -> exception.exceptionAttribute
            else -> ExceptionAttribute.make(exception, getStatus(request))
        }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun mediaTypeNotAcceptable(request: HttpServletRequest?): ResponseEntity<ExceptionAttribute> =
        getStatus(request).let { ResponseEntity.status(it).build() }
}
