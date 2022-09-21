package com.ryan.blogsearch.infrastructure.serialize

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.format.DateTimeFormatter


/**
 * [java.time.LocalDate]와 [java.time.LocalDateTime]을 기본 [DateTimeFormatterRegistrar]에서 지원할 수 있도록 기본 format에 대한
 * [DateTimeFormatter]를 추가해주는 Configuration.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Configuration
class DateTimeFormatConfiguration : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        val registrar = DateTimeFormatterRegistrar()
        registrar.setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE)
        registrar.setDateTimeFormatter(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        registrar.registerFormatters(registry)

        DateTimeFormatterRegistrar().apply {
            setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE)
            setDateTimeFormatter(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            registerFormatters(registry)
        }
    }
}
