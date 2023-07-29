package com.example.contactsbook.ui.sms

import com.example.contactsbook.models.SMSMessage

interface SmsItemClickListener {
    fun onSmsItemClick(item: SMSMessage)
}