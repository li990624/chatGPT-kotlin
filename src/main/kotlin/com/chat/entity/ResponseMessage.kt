package com.chat.entity

data class ResponseMessage(
    val uid: String?,
    val username: String?,
    val responseMessage: List<Messages>?,
    val content:String?,
    val role:String
)
