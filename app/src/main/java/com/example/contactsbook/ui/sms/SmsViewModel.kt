package com.example.contactsbook.ui.sms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SmsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is SMS Fragment"
    }
    val text: LiveData<String> = _text
}