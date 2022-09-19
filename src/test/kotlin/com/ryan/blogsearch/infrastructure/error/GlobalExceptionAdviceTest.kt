/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.error

import com.ninjasquad.springmockk.SpykBean
import com.ryan.blogsearch.infrastructure.error.model.DefinedExceptionAttribute
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Controller에서 [Exception]이 발생했을 경우 [GlobalExceptionAdvice] 를 통하여
 * 정의된 [com.ryan.blogsearch.infrastructure.error.model.ApiErrorResponse] 규격으로 [code], [message] 등이 정상적으로 생성 되는지 테스트한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
@WebMvcTest
@ContextConfiguration(
    classes = [GlobalExceptionAdvice::class, ExceptionTestController::class]
)
class GlobalExceptionAdviceTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @SpykBean
    lateinit var globalExceptionAdvice: GlobalExceptionAdvice

    @Test
    fun `expect 400 error response`() {
        mockMvc.perform(
            get("/test/400")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.BAD_REQUEST.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.BAD_REQUEST.message))
    }

    @Test
    fun `expect 405 error response`() {
        mockMvc.perform(
            get("/test/405")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(405))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.METHOD_NOT_ALLOWED.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.METHOD_NOT_ALLOWED.message))
    }

    @Test
    fun `expect 409 error response`() {
        mockMvc.perform(
            get("/test/409")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(409))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.BUSINESS_FAIL.code))
            .andExpect(jsonPath("message").value(ExceptionTestController.BUSINESS_EXCEPTION_MESSAGE))
    }

    @Test
    fun `expect 415 error response`() {
        mockMvc.perform(
            get("/test/415")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(415))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.UNSUPPORTED_MEDIA_TYPE.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.UNSUPPORTED_MEDIA_TYPE.message))
    }

    @Test
    fun `expect 500 error response`() {
        mockMvc.perform(
            get("/test/500")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().`is`(500))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.INTERNAL_SERVER_ERROR.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.INTERNAL_SERVER_ERROR.message))

        verify(exactly = 1) {
            globalExceptionAdvice.logUnexpectedServerError(any())
        }
    }
}
