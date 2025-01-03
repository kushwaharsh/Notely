package com.example.notesappwithhilt.commonUtils

object KeyConstants {

    const val SUCCESS = 200
    const val SUCCESSCODE = 201
    const val SECUTIRY_LOGOUT = 401
    const val TAG = "MYNOTESAPP"
    const val PREF_NAME = "notes_app1"
    const val BASE_URL = "https://notes-app-backend-lyart.vercel.app/api/"
   // const val BASE_URL = "https://notes-app-backend-n60srvw5u-kushwaharshs-projects.vercel.app/"
    const val REGISTER_USER = "auth/register"
    const val SIGN_IN = "auth/login"
    const val GET_ALL_NOTES = "notes/getAllnote"
    const val CREATE_NEW_NOTE = "notes/newNote"
    const val GET_NOTE_BY_ID = "notes/noteById"
    const val DELETE_NOTE = "notes/delete"
    const val UPDATE_NOTE = "notes/updateNote"
    const val CREATE_TAG = "tags/createTag"
    const val GET_ALL_TAGS = "tags/getAllTags"
    const val DELETE_ACCOUNT = "auth/user/deleteAccount"
    const val SEND_OTP = "auth/sendOtp"
    const val VERIFY_OTP = "auth/verifyOtp"
}