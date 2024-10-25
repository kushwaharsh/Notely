package com.example.notesappwithhilt.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappwithhilt.commonUtils.Resource
import com.example.notesappwithhilt.models.CreateNoteResponse
import com.example.notesappwithhilt.models.CreateTagResponse
import com.example.notesappwithhilt.models.GetAllNotesResponse
import com.example.notesappwithhilt.models.GetAllTagsResponse
import com.example.notesappwithhilt.models.NoteByIdResponse
import com.example.notesappwithhilt.models.SignUpResponse
import com.example.notesappwithhilt.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _registerUser : MutableLiveData<Resource<SignUpResponse?>?> = MutableLiveData()
    val registerUser : LiveData<Resource<SignUpResponse?>?>
        get() = _registerUser
    fun registerUser(params: HashMap<String , String>){
        viewModelScope.launch {
            _registerUser.value = Resource.Loading
            _registerUser.value = userRepository.registerUser(params)
        }
    }

    private val _signInUser : MutableLiveData<Resource<SignUpResponse?>?> = MutableLiveData()
    val signInUser : LiveData<Resource<SignUpResponse?>?>
        get() = _signInUser
    fun signInUser(params: HashMap<String, String>){
        viewModelScope.launch {
            _signInUser.value = Resource.Loading
            _signInUser.value = userRepository.signInUser(params)
        }
    }

    private val _getAllNotes : MutableLiveData<Resource<GetAllNotesResponse?>?> = MutableLiveData()
    val getAllNotes : LiveData<Resource<GetAllNotesResponse?>?>
        get() = _getAllNotes
    fun getAllNotes(token: String){
        viewModelScope.launch {
            _getAllNotes.value = Resource.Loading
            _getAllNotes.value = userRepository.getAllNotes(token)
        }
    }

    private val _createNewNote : MutableLiveData<Resource<CreateNoteResponse?>?> = MutableLiveData()
    val createNewNote : LiveData<Resource<CreateNoteResponse?>?>
        get() = _createNewNote
    fun createNewNote(token: String , params: HashMap<String, String>){
        viewModelScope.launch {
            _createNewNote.value = Resource.Loading
            _createNewNote.value = userRepository.createNewNote(token , params)
        }
    }

    private val _getNoteById : MutableLiveData<Resource<NoteByIdResponse?>?> = MutableLiveData()
    val getNoteById : LiveData<Resource<NoteByIdResponse?>?>
        get() = _getNoteById
    fun getNoteById(token: String , noteId: String){
        viewModelScope.launch {
            _getNoteById.value = Resource.Loading
            _getNoteById.value = userRepository.getNoteById(token , noteId)
        }
    }

    private val _deleteNote : MutableLiveData<Resource<CreateNoteResponse?>?> = MutableLiveData()
    val deleteNote : LiveData<Resource<CreateNoteResponse?>?>
        get() = _deleteNote
    fun deleteNote(token: String , noteId: String){
        viewModelScope.launch {
            _deleteNote.value = Resource.Loading
            _deleteNote.value = userRepository.deleteNote(token , noteId)
        }
    }

    private val _updateNote : MutableLiveData<Resource<NoteByIdResponse?>?> = MutableLiveData()
    val updateNote : LiveData<Resource<NoteByIdResponse?>?>
        get() = _updateNote
    fun updateNote(token: String , noteId: String , params: HashMap<String, String>){
        viewModelScope.launch {
            _updateNote.value = Resource.Loading
            _updateNote.value = userRepository.updateNote(token , noteId , params)
        }
    }

    private val _createTag : MutableLiveData<Resource<CreateTagResponse?>?> = MutableLiveData()
    val createTag : LiveData<Resource<CreateTagResponse?>?>
        get() = _createTag
    fun createTag(params: HashMap<String, String>){
        viewModelScope.launch {
            _createTag.value = Resource.Loading
            _createTag.value = userRepository.createTag(params)
        }
    }

    private val _getAllTags : MutableLiveData<Resource<GetAllTagsResponse?>?> = MutableLiveData()
    val getAllTags : LiveData<Resource<GetAllTagsResponse?>?>
        get() = _getAllTags
    fun getAllTags(userId : String){
        viewModelScope.launch {
            _getAllTags.value = Resource.Loading
            _getAllTags.value = userRepository.getAllTags(userId)
        }
    }
}