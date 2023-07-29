package com.example.contactsbook.ui.sms

import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.App
import com.example.contactsbook.models.SMSMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is SMS Fragment"
    }
    val text: LiveData<String> = _text

    private val _smsList = MutableLiveData<List<SMSMessage>>()
    val smsList: LiveData<List<SMSMessage>>
        get() = _smsList

    fun loadSMSMessages() {
        viewModelScope.launch {
            val messages = getSMSMessages()
            _smsList.postValue(messages)
        }
    }

    private suspend fun getSMSMessages(): List<SMSMessage> {
        return withContext(Dispatchers.IO) {
            val contentResolver = App.instance.contentResolver
            val uri = Telephony.Sms.CONTENT_URI

            val projection = arrayOf(
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
            )

            val cursor = contentResolver.query(uri, projection, null, null, null)
            val list = mutableListOf<SMSMessage>()

            if (cursor != null && cursor.moveToFirst()) {
                val indexAddress = cursor.getColumnIndex(Telephony.Sms.ADDRESS)
                val indexBody = cursor.getColumnIndex(Telephony.Sms.BODY)
                val indexDate = cursor.getColumnIndex(Telephony.Sms.DATE)

                do {
                    val address = cursor.getString(indexAddress)
                    val body = cursor.getString(indexBody)
                    val timestamp = cursor.getLong(indexDate)

                    val sms = SMSMessage(
                        sender = address,
                        body = body,
                        timestamp = timestamp
                    )

                    list.add(sms)
                } while (cursor.moveToNext())

                cursor.close()
            }

            list
        }
    }
}