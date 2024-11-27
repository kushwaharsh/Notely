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
import com.example.notesappwithhilt.models.SendOtpResponse
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

    private val _sendOtp : MutableLiveData<Resource<SendOtpResponse?>?> = MutableLiveData()
    val sendOtp : LiveData<Resource<SendOtpResponse?>?>
        get() = _sendOtp
    fun sendOtp(params: HashMap<String, String>){
        viewModelScope.launch {
            _sendOtp.value = Resource.Loading
            _sendOtp.value = userRepository.sendOtp(params)
        }
    }

    private val _verifyOtp : MutableLiveData<Resource<SignUpResponse?>?> = MutableLiveData()
    val verifyOtp : LiveData<Resource<SignUpResponse?>?>
        get() = _verifyOtp
    fun verifyOtp(params: HashMap<String, String>){
        viewModelScope.launch {
            _verifyOtp.value = Resource.Loading
            _verifyOtp.value = userRepository.verifyOtp(params)
        }
    }

    private val _getAllNotes : MutableLiveData<Resource<GetAllNotesResponse?>?> = MutableLiveData()
    val getAllNotes : LiveData<Resource<GetAllNotesResponse?>?>
        get() = _getAllNotes
    fun getAllNotes(token: String , userId: String){
        viewModelScope.launch {
            _getAllNotes.value = Resource.Loading
            _getAllNotes.value = userRepository.getAllNotes(token , userId)
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
    fun getNoteById(token: String , noteId: String , userId: String){
        viewModelScope.launch {
            _getNoteById.value = Resource.Loading
            _getNoteById.value = userRepository.getNoteById(token , noteId , userId)
        }
    }

    private val _deleteNote : MutableLiveData<Resource<CreateNoteResponse?>?> = MutableLiveData()
    val deleteNote : LiveData<Resource<CreateNoteResponse?>?>
        get() = _deleteNote
    fun deleteNote(token: String , noteId: String , userId: String){
        viewModelScope.launch {
            _deleteNote.value = Resource.Loading
            _deleteNote.value = userRepository.deleteNote(token , noteId , userId)
        }
    }

    private val _updateNote : MutableLiveData<Resource<NoteByIdResponse?>?> = MutableLiveData()
    val updateNote : LiveData<Resource<NoteByIdResponse?>?>
        get() = _updateNote
    fun updateNote(token: String , noteId: String , params: HashMap<String, String> , userId: String){
        viewModelScope.launch {
            _updateNote.value = Resource.Loading
            _updateNote.value = userRepository.updateNote(token , noteId , params , userId)
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
    fun getAllTags( token: String ,userId : String){
        viewModelScope.launch {
            _getAllTags.value = Resource.Loading
            _getAllTags.value = userRepository.getAllTags(token , userId)
        }
    }

    private val _deleteAccount : MutableLiveData<Resource<SignUpResponse?>?> = MutableLiveData()
    val deleteAccount : LiveData<Resource<SignUpResponse?>?>
        get() = _deleteAccount
    fun deleteAccount(token: String , userId: String){
        viewModelScope.launch {
            _deleteAccount.value = Resource.Loading
            _deleteAccount.value = userRepository.deleteAccount(token , userId)
        }
    }
}