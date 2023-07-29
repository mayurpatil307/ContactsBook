package com.example.contactsbook.models

data class SMSMessage(
    var sender: String,
    var body: String,
    var timestamp: Long
)
