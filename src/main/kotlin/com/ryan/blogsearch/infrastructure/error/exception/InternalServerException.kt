package com.ryan.blogsearch.infrastructure.error.exception

import com.ryan.blogsearch.infrastructure.error.model.DefinedExceptionAttribute
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * 서버 오류가 발생했을 경우를 위한 [DefinedException]
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerException :
    DefinedException(DefinedExceptionAttribute.INTERNAL_SERVER_ERROR) {
}
