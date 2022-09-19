/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.error

import com.ryan.blogsearch.infrastructure.error.exception.BusinessException
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Exception Test를 위한 Test Controller
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
@RestController
class ExceptionTestController {

    @GetMapping("/test/400")
    fun error400(@RequestParam param: String): String {
        return "test"
    }

    @PostMapping("/test/405")
    fun error405(): String {
        return "test"
    }

    @GetMapping("/test/409")
    fun error409(): String {
        throw BusinessException(BUSINESS_EXCEPTION_MESSAGE)
    }

    @GetMapping("/test/415", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun error415(): String {
        return "test"
    }

    @GetMapping("/test/500")
    fun error500(): Int {
        throw NullPointerException()
    }

    companion object {
        const val BUSINESS_EXCEPTION_MESSAGE = "테스트 메세지"
    }
}
