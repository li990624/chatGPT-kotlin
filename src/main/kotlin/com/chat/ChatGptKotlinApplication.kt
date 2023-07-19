package com.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatGptKotlinApplication

fun main(args: Array<String>) {
    runApplication<ChatGptKotlinApplication>(*args)
}
