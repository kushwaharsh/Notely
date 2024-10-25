package com.example.notesappwithhilt.models

data class CreateTagResponse(
    val label: Label?,
    val msg: String?,
    val statusCode: Int?,
    val success: Boolean?,
)

data class Label(
    val __v: Int?,
    val _id: String?,
    val userId: String?,
    val createdAt: String?,
    val tag: String?,
    val updatedAt: String?,
)