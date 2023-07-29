package com.example.contactsbook.ui.calls.incomingcalls

import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncomingCallsViewModel : ViewModel() {

    private val _incomingCallsList = MutableLiveData<List<CallLogItem>>()
    val incomingCallsList: LiveData<List<CallLogItem>> get() = _incomingCallsList

    fun fetchIncomingCalls(context: Context) {
        viewModelScope.launch {
            val incomingCalls = getIncomingCalls(context)
            _incomingCallsList.value = incomingCalls
        }
    }

    private suspend fun getIncomingCalls(context: Context): List<CallLogItem> {
        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER
            )

            val selection = "${CallLog.Calls.TYPE} = ?"
            val selectionArgs = arrayOf("${CallLog.Calls.INCOMING_TYPE}")

            val sortOrder = "${CallLog.Calls.DATE} DESC"

            val cursor: Cursor? = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            val incomingCallsList = mutableListOf<CallLogItem>()
            cursor?.use { c ->
                val nameColumn = c.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val numberColumn = c.getColumnIndex(CallLog.Calls.NUMBER)

                while (c.moveToNext()) {
                    val name = c.getString(nameColumn)
                    val number = c.getString(numberColumn)
                    incomingCallsList.add(CallLogItem(name, number, CallLogItem.CallType.INCOMING))
                }
            }

            cursor?.close()
            incomingCallsList
        }
    }
}
