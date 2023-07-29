package com.example.contactsbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _isPermissionsGranted = MutableLiveData<Boolean>()
    val isPermissionsGranted: LiveData<Boolean>
        get() = _isPermissionsGranted

    init {
        // Initialize the LiveData with a default value (false)
        _isPermissionsGranted.value = false
    }

    fun updatePermissionStatus(isGranted: Boolean) {
        // Update the LiveData with the permission status
        _isPermissionsGranted.value = isGranted
    }
}