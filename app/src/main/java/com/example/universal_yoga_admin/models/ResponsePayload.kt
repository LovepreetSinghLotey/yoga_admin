package com.example.universal_yoga_admin.models

data class ResponsePayload(
    val uploadResponseCode: String,
    val userId: String,
    val number: Int,
    val courses: String,
    val message: String
)