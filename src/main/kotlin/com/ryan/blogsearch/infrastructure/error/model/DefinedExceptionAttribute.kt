package com.ryan.blogsearch.infrastructure.error.model

import org.springframework.http.HttpStatus

private const val EMPTY_MESSAGE = ""

/**
 * 규격화 된 http status(400, 401, 403, 404, 405, 415, 409, 500) [ExceptionAttribute]
 * 해당 status의 별도 [ExceptionAttribute.message]를 사용하고 싶은 경우 [CustomizableExceptionAttribute]을 사용하여 별도로 구현한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
enum class DefinedExceptionAttribute(
    override val status: HttpStatus,
    override val message: String,
    override val code: String = status.name.lowercase(),
) : ExceptionAttribute {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패 하였습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청 주소가 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 Http-Method 입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 Content-Type 입니다."),
    BUSINESS_FAIL(HttpStatus.CONFLICT, EMPTY_MESSAGE),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 오류입니다.")
    ;
}

fun HttpStatus.toDefinedExceptionAttribute(): DefinedExceptionAttribute? =
    DefinedExceptionAttribute.values().singleOrNull() { it.status == this }
