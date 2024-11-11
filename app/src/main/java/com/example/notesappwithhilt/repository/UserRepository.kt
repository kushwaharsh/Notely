package com.example.notesappwithhilt.repository

import android.media.session.MediaSession.Token
import com.example.notesappwithhilt.api.ApiService
import com.example.notesappwithhilt.commonUtils.BaseRepository
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService ) : BaseRepository() {


    suspend fun registerUser(params: HashMap<String , String>) = safeApiCall{
        apiService.registerUser(params)
    }

    suspend fun signInUser(params: HashMap<String , String>) = safeApiCall{
        apiService.signInUser(params)
    }

    suspend fun getAllNotes(token: String , userId: String) = safeApiCall {
        apiService.getAllNotes(token , userId)
    }

    suspend fun createNewNote(token: String , params: HashMap<String, String>) = safeApiCall {
        apiService.createNewNote(token , params)
    }

    suspend fun getNoteById(token: String , noteId: String , userId: String) = safeApiCall {
        apiService.getNoteById(token , noteId , userId)
    }

    suspend fun deleteNote(token: String , noteId: String, userId: String) = safeApiCall {
        apiService.deleteNote(token , noteId , userId)
    }

    suspend fun updateNote(token: String , noteId: String , params: HashMap<String, String> , userId: String) = safeApiCall {
        apiService.updateNote(token ,noteId , params , userId)
    }

    suspend fun createTag(params: HashMap<String, String>) = safeApiCall {
        apiService.createTag(params)
    }

    suspend fun getAllTags(userId : String) = safeApiCall {
        apiService.getAllTags(userId)
    }
}