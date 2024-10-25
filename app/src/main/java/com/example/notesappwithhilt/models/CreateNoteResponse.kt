package com.example.notesappwithhilt.models

data class CreateNoteResponse(
    val msg: String?,
    val note: NewNote?,
    val statusCode: Int?,
    val success: Boolean?
)

data class NewNote(
    val __v: Int?,
    val _id: String?,
    val tag: String?,
    val isBookmarked: Boolean?,
    val content: String?,
    val whiteboard: String?,
    val createdAt: String?,
    val noteDeadline: String?,
    val dateCreated: String?,
    val lastModified: String?,
    val title: String?,
    val updatedAt: String?
)