package com.ryan.blogsearch.infrastructure.error.model

import org.springframework.http.HttpStatus

/**
 * [DefinedExceptionAttribute] 이외에 Error Message [ApiErrorResponse]로 전달 하기 위해 사용
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
class CustomizableExceptionAttribute(
    override val status: HttpStatus,
    override val message: String,
    override val code: String = status.name.lowercase(),
) : ExceptionAttribute
