package com.ryan.blogsearch.infrastructure.serialize

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Java8 [LocalDate], [LocalDateTime]의 Jackson Serialize/Deserialize를 위한 설정
 *
 * Spring boot properties 에서 제공하는
 *
 * spring.jackson.date-format
 * spring.jackson.time-zone
 *
 * 설정은 Java [Date] Type에 대해서만 지원하고, java 8 에서 추가된 time package내 class는 지원하지 못하므로
 * 아래와 같이 [Jackson2ObjectMapperBuilderCustomizer]를 이용하여 [LocalDate], [LocalDateTime]에 대한 설정을 추가한다
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Configuration
class JacksonJava8DateTimeFormatConfig : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(builder: Jackson2ObjectMapperBuilder?) {
        builder
            ?.timeZone(TimeZone.getDefault())
            ?.serializers(
                LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE),
                LocalDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
            ?.deserializers(
                LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE),
                LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
            ?.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}
