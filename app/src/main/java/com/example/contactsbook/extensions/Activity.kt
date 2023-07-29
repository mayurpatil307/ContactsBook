package com.example.contactsbook.extensions

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


/**
 * Check Permissions is Granted or not
 */
fun AppCompatActivity.isPermissionIsGranted(permission: String): Boolean {
    return when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) -> {
            true
        }
        else -> {
            false
        }
    }
}

/**
 * get request launcher instance
 */

fun AppCompatActivity.registerRequestLauncher(isPermissionGranted: ((isGranted: Boolean) -> Unit)): ActivityResultLauncher<String> {
    return registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        isPermissionGranted.invoke(isGranted)
    }
}