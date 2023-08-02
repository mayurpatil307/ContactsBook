package com.example.contactsbook.ui.calls.outgoingcalls

import android.database.Cursor
import android.provider.CallLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.App
import com.example.contactsbook.helpers.CallLogsHelper
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OutgoingCallsViewModel : ViewModel() {

    private val _outgoingCallsList = MutableLiveData<List<CallLogItem>>()
    val outgoingCallsList: LiveData<List<CallLogItem>> get() = _outgoingCallsList

    fun fetchOutgoingCallsList() {
        viewModelScope.launch {
            val calls = async { CallLogsHelper.getCallLogs(CallLog.Calls.OUTGOING_TYPE) }
            _outgoingCallsList.value = calls.await()
        }
    }
}
