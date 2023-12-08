package com.example.universal_yoga_admin.models

data class RequestPayload(
    val userId: String,
    val detailList: List<Course> = listOf()
)