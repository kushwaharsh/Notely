package com.example.notesappwithhilt.api

import com.example.notesappwithhilt.commonUtils.KeyConstants
import com.example.notesappwithhilt.models.CreateNoteResponse
import com.example.notesappwithhilt.models.CreateTagResponse
import com.example.notesappwithhilt.models.GetAllNotesResponse
import com.example.notesappwithhilt.models.GetAllTagsResponse
import com.example.notesappwithhilt.models.NewNote
import com.example.notesappwithhilt.models.NoteByIdResponse
import com.example.notesappwithhilt.models.SendOtpResponse
import com.example.notesappwithhilt.models.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST(KeyConstants.REGISTER_USER)
    suspend fun registerUser(@Body params: HashMap<String, String>): SignUpResponse?

    @POST(KeyConstants.VERIFY_OTP)
    suspend fun verifyOtp(@Body params: HashMap<String, String>): SignUpResponse?
    @POST(KeyConstants.SEND_OTP)
    suspend fun sendOtp(@Body params: HashMap<String, String>): SendOtpResponse?

    @GET(KeyConstants.GET_ALL_NOTES)
    suspend fun getAllNotes(@Header("Authorization") token: String,
                            @Query("userId") userId: String) : GetAllNotesResponse?

    @POST(KeyConstants.CREATE_NEW_NOTE)
    suspend fun createNewNote(
        @Header("Authorization") token: String,
        @Body params: HashMap<String, String>) : CreateNoteResponse?

    @GET(KeyConstants.GET_NOTE_BY_ID)
    suspend fun getNoteById(@Header("Authorization") token: String,
                            @Query("id") noteId: String,
                            @Query("userId") userId: String) : NoteByIdResponse

    @DELETE(KeyConstants.DELETE_NOTE)
    suspend fun deleteNote(@Header("Authorization") token: String,
                            @Query("id") noteId: String,
                           @Query("userId") userId: String) : CreateNoteResponse

    @POST(KeyConstants.UPDATE_NOTE)
    suspend fun updateNote(@Header("Authorization") token: String,
                           @Query("id") noteId: String,
                           @Body params: HashMap<String, String>,
                           @Query("userId") userId: String) : NoteByIdResponse

    @POST(KeyConstants.CREATE_TAG)
    suspend fun createTag(@Body params: HashMap<String, String>) : CreateTagResponse

    @GET(KeyConstants.GET_ALL_TAGS)
    suspend fun getAllTags(@Header("Authorization") token: String,
                           @Query("userId") userId : String) : GetAllTagsResponse

    @GET(KeyConstants.DELETE_ACCOUNT)
    suspend fun deleteAccount(@Header("Authorization") token: String,
                              @Query("userId") userId : String) : SignUpResponse


}