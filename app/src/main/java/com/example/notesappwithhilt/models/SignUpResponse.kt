package com.example.notesappwithhilt.models

data class SignUpResponse(
    val `data`: LoginData?,
    val msg: String?,
    val statusCode: Int?,
    val success: Boolean?,
    val token: String?
)

data class LoginData(
    val __v: Int?,
    val _id: String?,
    val date: String?,
    val email: String?,
    val name: String?,
    val password: String?
)