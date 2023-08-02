package com.example.contactsbook.helpers

import android.database.Cursor
import android.provider.CallLog
import com.example.contactsbook.App
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CallLogsHelper {

    suspend fun getCallLogs(type: Int): List<CallLogItem> {
        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER
            )

            val selection = "${CallLog.Calls.TYPE} = ?"
            val selectionArgs = arrayOf("$type") //${CallLog.Calls.INCOMING_TYPE}

            val sortOrder = "${CallLog.Calls.DATE} DESC"

            val cursor: Cursor? = App.instance.contentResolver.query(
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
                    incomingCallsList.add(CallLogItem(name, number))
                }
            }

            cursor?.close()
            incomingCallsList
        }
    }
}