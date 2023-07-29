package com.example.contactsbook.models

data class CallLogItem(
    val callerName: String?,
    val callerNumber: String,
    val callType: CallType
) {
    enum class CallType {
        INCOMING,
        OUTGOING,
        MISSED,
    }
}

