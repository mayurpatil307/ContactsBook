package com.example.contactsbook.ui.calls.missedcalls

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

class MissedCallsViewModel : ViewModel() {

    private val _missedCallsList = MutableLiveData<List<CallLogItem>>()
    val missedCallsList: LiveData<List<CallLogItem>> get() = _missedCallsList

    fun fetchMissedCallsList() {
        viewModelScope.launch {
            val incomingCalls = getMissedCalls()
            _missedCallsList.value = incomingCalls
        }
    }

    private suspend fun getMissedCalls(): List<CallLogItem> {
        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER
            )

            val selection = "${CallLog.Calls.TYPE} = ?"
            val selectionArgs = arrayOf("${CallLog.Calls.MISSED_TYPE}")

            val sortOrder = "${CallLog.Calls.DATE} DESC"

            val cursor: Cursor? = App.instance.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            val callsList = mutableListOf<CallLogItem>()
            cursor?.use { c ->
                val nameColumn = c.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val numberColumn = c.getColumnIndex(CallLog.Calls.NUMBER)

                while (c.moveToNext()) {
                    val name = c.getString(nameColumn)
                    val number = c.getString(numberColumn)
                    callsList.add(CallLogItem(name, number, CallLogItem.CallType.MISSED))
                }
            }

            cursor?.close()
            callsList
        }
    }
}
