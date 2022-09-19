package com.ryan.blogsearch.infrastructure.error.model

/**
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
data class ApiErrorResponse(
    val code: String,
    val message: String,
)
