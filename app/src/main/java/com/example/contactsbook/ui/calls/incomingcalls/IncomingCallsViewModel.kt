package com.example.contactsbook.ui.calls.incomingcalls

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

class IncomingCallsViewModel : ViewModel() {

    private val _incomingCallsList = MutableLiveData<List<CallLogItem>>()
    val incomingCallsList: LiveData<List<CallLogItem>> get() = _incomingCallsList

    fun fetchIncomingCalls() {
        viewModelScope.launch {
            val incomingCalls = async { CallLogsHelper.getCallLogs(CallLog.Calls.INCOMING_TYPE) }
            _incomingCallsList.value = incomingCalls.await()
        }
    }
}
