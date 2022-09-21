package com.ryan.blogsearch.infrastructure.business.search.service

import com.ninjasquad.springmockk.MockkBean
import com.ryan.blogsearch.business.search.client.kakao.KakaoApiException
import com.ryan.blogsearch.business.search.client.kakao.KakaoBLogClient
import com.ryan.blogsearch.business.search.client.kakao.KakaoBlogModel
import com.ryan.blogsearch.business.search.client.kakao.KakaoDaumApiError
import com.ryan.blogsearch.business.search.model.SearchBlog
import com.ryan.blogsearch.business.search.repository.SearchHistoryRepository
import com.ryan.blogsearch.business.search.service.SearchHistoryDataHandler
import com.ryan.blogsearch.business.search.service.SearchIntegrationApi
import com.ryan.blogsearch.business.search.service.cache.SearchHistoryCacheManager
import com.ryan.blogsearch.infrastructure.error.exception.BusinessException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.util.AssertionErrors.assertEquals
import java.net.UnknownHostException
import java.time.LocalDateTime

/**
 *
 * @author M.Ryan (sh.ryan.park@dreamus.io)
 * @since 2022-09-21
 */

@SpringBootTest(classes = [SearchHistoryDataHandler::class, SearchIntegrationApi::class])
class SearchIntegrationApiTest {

    @MockkBean
    lateinit var searchHistoryRepository: SearchHistoryRepository

    @MockkBean
    lateinit var kakaoBLogClient: KakaoBLogClient

    @MockkBean
    lateinit var searchHistoryCacheManager: SearchHistoryCacheManager

    @Autowired
    lateinit var searchIntegrationApi: SearchIntegrationApi;

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Autowired
    lateinit var searchHistoryDataHandler: SearchHistoryDataHandler

    @Test
    fun `expect kakaoapi response transform to blog response and record history by eventListenr`() {
        val mockResponse = mockKakaoBlogResponse()
        every {
            kakaoBLogClient.searchBLog(any())
        } returns mockResponse

        every { searchHistoryRepository.findByIdOrNull(any()) } returns null
        every { searchHistoryRepository.save(any()) } returns mockk()

        val result: SearchBlog.Response = searchIntegrationApi.search(SearchBlog.Request("test"))

        verify {
            searchHistoryRepository.findByIdOrNull(any())
            searchHistoryRepository.save(any())
        }

        assertEquals("document.title", result.results[0].title, mockResponse.documents[0].title)
        assertEquals("document.contents", result.results[0].contents, mockResponse.documents[0].contents)
        assertEquals("document.url", result.results[0].link, mockResponse.documents[0].url)
        assertEquals("document.blogname", result.results[0].blogName, mockResponse.documents[0].blogname)
        assertEquals("document.datetime", result.results[0].postDate, mockResponse.documents[0].datetime.toLocalDate())
    }

    @Test
    fun `throw exception by kakao api KakaoApiException`() {
        every {
            kakaoBLogClient.searchBLog(any())
        } throws KakaoApiException("fail", KakaoDaumApiError("testErrorType", "message"))

        assertThrows<BusinessException> {
            searchIntegrationApi.search(SearchBlog.Request("test"))
        }
    }

    @Test
    fun `throw exception by kakao api UnknownHostException`() {
        every {
            kakaoBLogClient.searchBLog(any())
        } throws UnknownHostException()

        assertThrows<UnknownHostException> {
            searchIntegrationApi.search(SearchBlog.Request("test"))
        }
    }

    fun mockKakaoBlogResponse() = KakaoBlogModel.Response(
        meta = KakaoBlogModel.MetaDto(100, 10, false),
        documents = listOf(
            KakaoBlogModel.DocumentDto(
                title = "title",
                contents = "contents",
                url = "url",
                blogname = "blogname",
                thumbnail = "thumbnail",
                datetime = LocalDateTime.MIN
            )
        )
    )
}
