package com.example.notesappwithhilt.models

data class GetAllTagsResponse(
    val labels: List<TagList>?,
    val msg: String?,
    val statusCode: Int?,
    val success: Boolean?
)

data class TagList(
    val __v: Int?,
    val _id: String?,
    val createdAt: String?,
    val tag: String?,
    val updatedAt: String?,
    val userId: String?
)