package com.ryan.blogsearch.business.search.client.kakao

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * kakao daum api 설정 정보.
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@Component
@ConfigurationProperties(prefix = "kakao-daum")
class KakaoDaumApiProperties {
    val api: Api = Api()

    class Api {
        lateinit var url: String
        lateinit var key: String
    }
}
