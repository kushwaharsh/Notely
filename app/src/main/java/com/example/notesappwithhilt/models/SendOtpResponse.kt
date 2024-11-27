package com.example.notesappwithhilt.models

data class SendOtpResponse(
    val isExists: Boolean?,
    val msg: String?,
    val statusCode: Int?,
    val success: Boolean?,
    val email : String?
)