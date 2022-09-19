/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.error.exception

import com.ryan.blogsearch.infrastructure.error.model.CustomizableExceptionAttribute
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * 서비스 요구 사항을 만족시키지 못하여 409 응답이 필요할 경우 사용할 수 있는 [DefinedException]
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
open class BusinessException : DefinedException {
    constructor(message: String) : super(CustomizableExceptionAttribute(HttpStatus.CONFLICT, message))
    constructor(message: String, throwable: Throwable) : super(
        CustomizableExceptionAttribute(
            HttpStatus.CONFLICT,
            message
        ), throwable
    )
}
