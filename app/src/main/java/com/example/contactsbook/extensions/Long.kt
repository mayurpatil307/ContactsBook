package com.example.contactsbook.extensions

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun Long.toPresentableTime(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        // For Android API level 26 and higher, use java.time package
        val instant = Instant.ofEpochMilli(this)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } else {
        // For Android API level lower than 26, use SimpleDateFormat
        val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
        sdf.format(Date(this))
    }
}