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

    suspend fun getAllNotes(token: String) = safeApiCall {
        apiService.getAllNotes(token)
    }

    suspend fun createNewNote(token: String , params: HashMap<String, String>) = safeApiCall {
        apiService.createNewNote(token , params)
    }

    suspend fun getNoteById(token: String , noteId: String) = safeApiCall {
        apiService.getNoteById(token , noteId)
    }

    suspend fun deleteNote(token: String , noteId: String) = safeApiCall {
        apiService.deleteNote(token , noteId)
    }

    suspend fun updateNote(token: String , noteId: String , params: HashMap<String, String>) = safeApiCall {
        apiService.updateNote(token ,noteId , params)
    }

    suspend fun createTag(params: HashMap<String, String>) = safeApiCall {
        apiService.createTag(params)
    }

    suspend fun getAllTags(userId : String) = safeApiCall {
        apiService.getAllTags(userId)
    }
}