package com.example.contactsbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isPermissionsGranted = MutableLiveData<Boolean>()
    val isPermissionsGranted: LiveData<Boolean>
        get() = _isPermissionsGranted

    private val _refreshEventSharedFlow = MutableSharedFlow<Boolean>()
    val refreshEventSharedFlow: SharedFlow<Boolean>
        get() = _refreshEventSharedFlow

    init {
        _isPermissionsGranted.value = false
    }

    fun updatePermissionStatus(isGranted: Boolean) {
        _isPermissionsGranted.value = isGranted
    }

    fun emitRefreshEvent(isRefresh: Boolean) {
        viewModelScope.launch {
            _refreshEventSharedFlow.emit(isRefresh)
        }
    }
}