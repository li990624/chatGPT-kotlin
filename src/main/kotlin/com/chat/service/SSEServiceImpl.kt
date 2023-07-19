package com.chat.service

import com.chat.entity.ChatCompletionChunk
import com.chat.entity.Messages
import com.chat.entity.ResponseMessage
import com.google.gson.Gson
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * @author xiansen
 */
@Service
class SSEServiceImpl(
    private val sink: Sinks.Many<ResponseMessage> = Sinks.many().multicast().onBackpressureBuffer()
) {

    fun getStreamData(): Flux<ResponseMessage> {
        return sink.asFlux()
    }

    fun sendData(message: ResponseMessage) {
        val httpClient = HttpClient.newBuilder()
            .proxy(ProxySelector.of(InetSocketAddress("127.0.0.1", 10809))) // 设置代理
            .build()
        val apiUrl = "https://api.openai.com/v1/chat/completions"
        val apiKey = "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"
        val requestBody = mutableMapOf<String, Any?>()
        requestBody["model"] = "gpt-3.5-turbo"
        requestBody["messages"] = message.responseMessage
        // 添加其他参数
        requestBody["temperature"] = 1
        requestBody["max_tokens"] = 3000
        requestBody["top_p"] = 1
        requestBody["frequency_penalty"] = 0
        requestBody["presence_penalty"] = 0
        requestBody["stream"] = true // 添加支持流式返回的字段
        val gson = Gson()
        val jsonBody: String = gson.toJson(requestBody)
        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build()
        try {
            val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream())
            val reader = BufferedReader(InputStreamReader(response.body()))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line = line?.replaceFirst("data: ".toRegex(), "")
                println(line)
                if (line?.contains("DONE") == false) {
                    val data = gson.fromJson(line, ChatCompletionChunk::class.java)
                    if (data!=null && data.choices[0].delta.content != null ) {
                        println(message.uid)
                        sink.tryEmitNext( message.copy(uid = message.uid, content = data.choices[0].delta.content))
                    }
                }else{
                    sink.tryEmitNext( message.copy(responseMessage = listOf(Messages(line,"user"))))
                }
            }
            reader.close()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}
