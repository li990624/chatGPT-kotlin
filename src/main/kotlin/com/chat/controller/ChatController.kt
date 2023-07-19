package com.chat.controller

import com.chat.entity.ResponseMessage
import com.chat.service.SSEServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.io.IOException

@RequestMapping("api/chat")
@RestController
class ChatController @Autowired constructor(
    private var sseService: SSEServiceImpl
) {

    @GetMapping(value = ["endpoint"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamData(): Flux<ResponseMessage> {
        return sseService.getStreamData()

    }

    @PostMapping("send")
    fun sendData(@RequestPart("message") message: ResponseMessage): String {
        try {
            sseService.sendData(message)
            return "已发送!"
        } catch (e: IOException) {
            throw RuntimeException("发送失败!")
        }

    }

    @PostMapping("test")
    fun test(@RequestParam("data") data: String): String {
        return data
    }
}