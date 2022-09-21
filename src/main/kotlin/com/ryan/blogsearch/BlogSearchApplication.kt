package com.ryan.blogsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class BlogSearchApplication

fun main(args: Array<String>) {
    runApplication<BlogSearchApplication>(*args)
}
