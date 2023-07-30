package com.example.contactsbook.ui.calls.outgoingcalls

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.App
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OutgoingCallsViewModel : ViewModel() {

    private val _outgoingCallsList = MutableLiveData<List<CallLogItem>>()
    val outgoingCallsList: LiveData<List<CallLogItem>> get() = _outgoingCallsList

    fun fetchOutgoingCallsList() {
        viewModelScope.launch {
            val incomingCalls = getOutgoingCalls()
            _outgoingCallsList.value = incomingCalls
        }
    }

    private suspend fun getOutgoingCalls(): List<CallLogItem> {
        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER
            )

            val selection = "${CallLog.Calls.TYPE} = ?"
            val selectionArgs = arrayOf("${CallLog.Calls.OUTGOING_TYPE}")

            val sortOrder = "${CallLog.Calls.DATE} DESC"

            val cursor: Cursor? = App.instance.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            val outgoinCallsList = mutableListOf<CallLogItem>()
            cursor?.use { c ->
                val nameColumn = c.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val numberColumn = c.getColumnIndex(CallLog.Calls.NUMBER)

                while (c.moveToNext()) {
                    val name = c.getString(nameColumn)
                    val number = c.getString(numberColumn)
                    outgoinCallsList.add(CallLogItem(name, number, CallLogItem.CallType.OUTGOING))
                }
            }

            cursor?.close()
            outgoinCallsList
        }
    }
}
