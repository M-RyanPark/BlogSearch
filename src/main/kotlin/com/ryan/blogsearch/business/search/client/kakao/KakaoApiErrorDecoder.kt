package com.ryan.blogsearch.business.search.client.kakao

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import feign.Response
import feign.Util
import feign.codec.ErrorDecoder
import mu.KLogging
import org.springframework.http.HttpStatus
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * 카카오API 응답의 http status 400~500 응답을 Decoding 하기 위한 [ErrorDecoder]
 * resoponse json {"errorType":"xxx", "message":"yyyy"} 형태를 [KakaoDaumApiError]로 구성하여 [KakaoApiException]으로 응답한다.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
class KakaoApiErrorDecoder : ErrorDecoder {

    companion object : KLogging() {
        val objectMapper: ObjectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .registerKotlinModule()
    }

    override fun decode(methodKey: String, response: Response): Exception {
        return parseError(methodKey, response)
    }

    private fun parseError(methodKey: String, response: Response): KakaoApiException {
        val status = HttpStatus.valueOf(response.status())
        var message = String.format("status %s reading %s", response.status(), methodKey)
        return try {
            if (response.body() != null) {
                val body = Util.toString(response.body().asReader(StandardCharsets.UTF_8))
                message += "; content:\n$body"
                val res: KakaoDaumApiError = convertJsonStringToObject(body, KakaoDaumApiError::class.java)
                if (status.is5xxServerError) {
                    logger.error("{} 서버 응답 : {}", methodKey, res)
                }

                KakaoApiException(message, res)
            } else {
                logger.error("{} 연동 에러 : body 없음", methodKey)
                KakaoApiException("No body", KakaoDaumApiError("InternalError", "Link Error"))
            }
        } catch (e: Exception) {
            logger.error("{} 연동 에러 ", methodKey, e)
            KakaoApiException(message, KakaoDaumApiError("InternalError", e.message ?: "Error"))
        }
    }

    @Throws(IOException::class)
    fun <T> convertJsonStringToObject(value: String, clz: Class<T>): T {
        return objectMapper.readValue(value, clz)
    }
}

class KakaoApiException(
    val rawMessage: String,
    val error: KakaoDaumApiError,
) : RuntimeException(rawMessage)
