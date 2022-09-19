package com.ryan.blogsearch.infrastructure.error.exception

import com.ryan.blogsearch.infrastructure.error.model.ExceptionAttribute

/**
 * Framework 내에서 사용하기 위한 기본 [Exception] class
 * 자체적으로 정의하는 [Exception]은 [DefinedException]을 모두 상속할 수 있도록 한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
abstract class DefinedException : RuntimeException {
    val exceptionAttribute: ExceptionAttribute

    protected constructor(exceptionAttribute: ExceptionAttribute) : super(exceptionAttribute.message) {
        this.exceptionAttribute = exceptionAttribute
    }

    protected constructor(exceptionAttribute: ExceptionAttribute, cause: Throwable) : super(
        exceptionAttribute.message,
        cause
    ) {
        this.exceptionAttribute = exceptionAttribute
    }
}
