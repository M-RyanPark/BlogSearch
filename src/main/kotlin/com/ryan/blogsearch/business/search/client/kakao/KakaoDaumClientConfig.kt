package com.ryan.blogsearch.business.search.client.kakao

import feign.RequestInterceptor
import feign.form.FormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope

/**
 * [KakaoBLogClient] 사용을 위한 feign-configuration
 *
 * 카카오 API는 application/x-www-form-urlencoded 를 사용하기 때문에 POJO class를 이용하여 요청을 처리하기 위해서 [FormEncoder]를 설정해야 한다.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
class KakaoDaumClientConfig(
    private val properties: KakaoDaumApiProperties,
    private val messageConverters: ObjectFactory<HttpMessageConverters>,
) {
    @Bean
    fun authorizationInterceptor(): RequestInterceptor =
        RequestInterceptor {
            it.header("Authorization", "KakaoAK ${properties.api.key}")
        }

    @Bean
    @Primary
    @Scope(SCOPE_PROTOTYPE)
    fun feignFormEncoder(): FormEncoder = FormEncoder(SpringEncoder(this.messageConverters))
}
