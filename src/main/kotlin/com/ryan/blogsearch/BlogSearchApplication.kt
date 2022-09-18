package com.ryan.blogsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogSearchApplication

fun main(args: Array<String>) {
    runApplication<BlogSearchApplication>(*args)
}
