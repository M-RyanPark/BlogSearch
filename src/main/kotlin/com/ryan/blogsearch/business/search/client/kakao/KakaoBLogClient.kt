package com.ryan.blogsearch.business.search.client.kakao

import com.ryan.blogsearch.infrastructure.feign.FeignRequestResponseLogger
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping

/**
 *
 * @author Sanghyun Park (sanghyun.ryan.park@gmail.com)
 * @since 2022-09-21
 */
@FeignClient(
    name = "kakao-blog-api",
    url = "\${kakao-daum.api.url}",
    configuration = [
        KakaoApiErrorDecoder::class,
        KakaoDaumClientConfig::class,
        FeignRequestResponseLogger::class,
        FeignRequestResponseLogger.FeignClientLogLevelConfig::class,
    ]
)
interface KakaoBLogClient {
    @GetMapping("/v2/search/blog")
    fun searchBLog(@SpringQueryMap request: KakaoBlogModel.Request): KakaoBlogModel.Response
}
