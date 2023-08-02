package com.example.contactsbook.ui.calls.missedcalls

import android.provider.CallLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.helpers.CallLogsHelper
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MissedCallsViewModel : ViewModel() {

    private val _missedCallsList = MutableLiveData<List<CallLogItem>>()
    val missedCallsList: LiveData<List<CallLogItem>> get() = _missedCallsList

    fun fetchMissedCallsList() {
        viewModelScope.launch {
            val calls = async { CallLogsHelper.getCallLogs(CallLog.Calls.MISSED_TYPE) }
            _missedCallsList.value = calls.await()
        }
    }
}
