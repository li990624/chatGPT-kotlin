package com.chat.entity

/**
 * @author xiansen
 */
data class ChatCompletionChunk(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val index: Int,
    val delta: Delta,
    val finish_reason: String
)

data class Delta(
   val content:String
)
