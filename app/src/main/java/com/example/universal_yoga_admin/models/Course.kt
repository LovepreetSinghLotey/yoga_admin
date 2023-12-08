package com.example.universal_yoga_admin.models

enum class TypeOfClass {
    FLOW, AERIAL, FAMILY,
    PRIVATE, CORPORATE, KIDS
}

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val courseImage: String,
    val dayOfWeek: String,
    val timeOfDay: String,
    val capacity: Int,
    val duration: Double,
    val pricePerClass: Double,
    val typeOfClass: String,
    val classList: List<Class> = listOf(),
    val createdBy: String,
    val createdOn: String,
    val updatedBy: String? = null,
    val updatedOn: String? = null
)
