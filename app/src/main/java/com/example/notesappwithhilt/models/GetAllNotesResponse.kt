package com.example.notesappwithhilt.models

data class GetAllNotesResponse(
    val notes: List<Note>,
    val statusCode: Int,
    val success: Boolean
)

data class Note(
    val __v: Int,
    val _id: String,
    val tag: String?,
    val isBookmarked: Boolean?,
    val content: String,
    val whiteboard: String,
    val noteDeadline: String?,
    val dateCreated: String,
    val lastModified: String,
    val title: String,
    val userId: String
)