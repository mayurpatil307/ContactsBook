package com.example.contactsbook.models

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
