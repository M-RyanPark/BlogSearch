package com.ryan.blogsearch.infrastructure.feign

import feign.Logger
import feign.Request
import feign.Response
import feign.Util
import mu.KLogging
import org.springframework.context.annotation.Bean
import java.io.IOException
import java.util.*

/**
 * FeignClient의 기본 Logger([feign.slf4j.Slf4jLogger])는 client interface 의 로그 레벨이 debug일 때만 작동하고
 * request/response 를 자세히 보기 위해 feign.loggerLevel 을 FULL 로 설정할 경우 여러 줄에 걸쳐 log가 기록된다.
 * <p>
 * [Logger]를 extends 하여 별도 설정 없이 info 레벨에서 로깅 할 수 있고, request/response log format을 최적화 한다
 * [Logger]구현체를 [Component]등을 사용하여 bean으로 등록하면, [org.springframework.cloud.openfeign.DefaultFeignLoggerFactory]에 의하여
 * 모든 feign-client에 전역 설정되지만, 이 Logger의 사용을 원하지 않는 경우도 있을수 있으므로 bean 등록을 통한 전역 설정은 팀원들과 협의하여 진행한다.
 * bean으로 등록하지 않더라도 [FeignClient]에 추가하면 개별 단위로 적용 가능하다.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @see org.springframework.cloud.openfeign.DefaultFeignLoggerFactory
 * @see org.springframework.cloud.openfeign.FeignClientsConfiguration
 * @since 2022-09-21
 */
class FeignRequestResponseLogger : Logger() {
    companion object : KLogging() {
        const val EMPTY = "EMPTY"
        const val MAX_BODY_SIZE = 100
    }

    override fun logRequest(configKey: String?, logLevel: Level?, request: Request) {
        log(
            configKey,
            "--> %s %s headers=[%s] body=[%s]",
            request.httpMethod(),
            request.url(),
            extractRequestHeader(request),
            extractRequestBody(request)
        )
    }

    private fun extractRequestHeader(request: Request): String? {
        val list = ArrayList<String>()
        for (field in request.headers().keys) {
            for (value in Util.valuesOrEmpty(request.headers(), field)) {
                list.add("$field=$value")
            }
        }
        return if (list.isEmpty()) EMPTY else list.joinToString(",")
    }

    private fun extractRequestBody(request: Request): String? {
        return if (request.body() != null && request.charset() != null) String(
            request.body(),
            request.charset()
        ) else EMPTY
    }

    @Throws(IOException::class)
    override fun logAndRebufferResponse(
        configKey: String?,
        logLevel: Level?,
        response: Response,
        elapsedTime: Long
    ): Response? {
        val status = response.status()
        // HTTP 205 Reset Content "...response MUST NOT include an entity"
        return if (response.body() != null && status != 205) {
            val bodyData = Util.toByteArray(response.body().asInputStream())
            log(
                configKey,
                "<-- %s (%sms) body=[%s]",
                status,
                elapsedTime,
                Util.decodeOrDefault(bodyData, Util.UTF_8, EMPTY).substring(0, MAX_BODY_SIZE)
            )
            response.toBuilder().body(bodyData).build()
        } else {
            log(configKey, "<-- %s (%sms)", status, elapsedTime)
            response
        }
    }

    override fun log(configKey: String?, format: String, vararg args: Any?) {
        logger.info(String.format(methodTag(configKey) + format, *args))
    }

    /**
     * feign [Logger.Level] 이 NONE일 경우 로그가 찍히지 않기 때문에 BASIC으로 설정하기 위한 configuration class
     */
    class FeignClientLogLevelConfig {
        @Bean
        fun feignLoggerLevel(): Level = Level.BASIC
    }
}
