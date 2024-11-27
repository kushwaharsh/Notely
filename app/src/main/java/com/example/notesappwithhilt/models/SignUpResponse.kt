package com.example.notesappwithhilt.models

data class SignUpResponse(
    val `data`: LoginData?,
    val msg: String?,
    val statusCode: Int?,
    val success: Boolean?,
    val token: String?,
    val isExists : Boolean?
)

data class LoginData(
    val __v: Int?,
    val _id: String?,
    val collaboratedMembers: List<Any?>?,
    val collaborationStatus: String?,
    val date: String?,
    val email: String?,
    val id: String?,
    val name: String?,
    val subscriptionPlanStatus: String?
)