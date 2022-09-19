/*
 * Copyright (c) 2021 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.ryan.blogsearch.infrastructure.error

import com.ryan.blogsearch.infrastructure.error.exception.BadRequestException
import com.ryan.blogsearch.infrastructure.error.exception.DefinedException
import com.ryan.blogsearch.infrastructure.error.model.DefinedExceptionAttribute
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * [ApiErrorController] test
 *
 * Controller 외에(Dispatcher Servlet 등) Exception이 발생할 경우
 * 정의된 [com.ryan.blogsearch.infrastructure.error.model.ApiErrorResponse] 규격으로 response를 잘 생성하는지 확인한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-19
 */
@WebMvcTest(ApiErrorController::class)
@ContextConfiguration(classes = [])
class ErrorControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `expect 400 error response`() {
        mockMvc.perform(
            get("/error")
                .accept(MediaType.APPLICATION_JSON)
                .with {
                    it.apply {
                        this.setAttribute(ERROR_ATTRIBUTE, BadRequestException(BAD_REQUEST_ERROR_MSG))
                    }
                }
        )
            .andExpect(status().`is`(400))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.BAD_REQUEST.code))
            .andExpect(jsonPath("message").value(BAD_REQUEST_ERROR_MSG))
    }


    @Test
    fun `expect 401 error response`() {
        mockMvc.perform(
            get("/error")
                .accept(MediaType.APPLICATION_JSON)
                .with {
                    it.apply {
                        this.setAttribute(ERROR_ATTRIBUTE, UnAuthenticatedException())
                    }
                }
        )
            .andExpect(status().`is`(401))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.UNAUTHORIZED.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.UNAUTHORIZED.message))
    }

    @Test
    fun `expect 403 error response`() {
        mockMvc.perform(
            get("/error")
                .accept(MediaType.APPLICATION_JSON)
                .with {
                    it.apply {
                        this.setAttribute(ERROR_ATTRIBUTE, UnAuthorizedException())
                    }
                }
        )
            .andExpect(status().`is`(403))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.FORBIDDEN.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.FORBIDDEN.message))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `expect 404 error response`() {
        mockMvc.perform(
            get("/error")
                .accept(MediaType.APPLICATION_JSON)
                .with {
                    it.apply {
                        this.setAttribute(ERROR_ATTRIBUTE, UrlNotFoundException())
                    }
                }
        )
            .andExpect(status().`is`(404))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.NOT_FOUND.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.NOT_FOUND.message))
    }

    @Test
    fun `expect 500 error response`() {
        mockMvc.perform(
            get("/error")
                .accept(MediaType.APPLICATION_JSON)
                .with {
                    it.apply {
                        this.setAttribute(ERROR_ATTRIBUTE, NullPointerException())
                    }
                }
        )
            .andExpect(status().`is`(500))
            .andExpect(jsonPath("code").value(DefinedExceptionAttribute.INTERNAL_SERVER_ERROR.code))
            .andExpect(jsonPath("message").value(DefinedExceptionAttribute.INTERNAL_SERVER_ERROR.message))
    }

    companion object {
        val ERROR_ATTRIBUTE = DefaultErrorAttributes::class.java.name + ".ERROR";
        const val BAD_REQUEST_ERROR_MSG = "bad request"
    }

    class UrlNotFoundException : DefinedException(DefinedExceptionAttribute.NOT_FOUND)
    class UnAuthenticatedException : DefinedException(DefinedExceptionAttribute.UNAUTHORIZED)
    class UnAuthorizedException : DefinedException(DefinedExceptionAttribute.FORBIDDEN)
}
