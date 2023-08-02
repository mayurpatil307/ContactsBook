package com.example.contactsbook

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _refreshEventSharedFlow = MutableSharedFlow<Boolean>()
    val refreshEventSharedFlow: SharedFlow<Boolean>
        get() = _refreshEventSharedFlow

    val permissionLiveEvent = SingleLiveEvent<Boolean>()
    val showToastEvent = SingleLiveEvent<String>()
    val navEvent = SingleLiveEvent<Int>()

    fun updatePermissionResult(isGranted: Boolean) {
        viewModelScope.launch {
            permissionLiveEvent.postValue(isGranted)
        }
    }

    fun emitRefreshEvent(isRefresh: Boolean) {
        viewModelScope.launch {
            _refreshEventSharedFlow.emit(isRefresh)
        }
    }

    fun emitToastEvent(msg: String) {
        showToastEvent.postValue(msg)
    }

    fun saveLastVisitedItemId(itemId: Int) {
        val sharedPrefs = App.instance.getSharedPreferences("last_visited", Context.MODE_PRIVATE)
        sharedPrefs.edit().putInt("last_visited_item_id", itemId).apply()
    }

    fun getLastVisitedItemId(): Int {
        val sharedPrefs =
            App.instance.getSharedPreferences(LAST_VISITED, Context.MODE_PRIVATE)
        return sharedPrefs.getInt(LAST_VISITED_ITEM_ID, R.id.nav_contacts)
    }

    companion object {
        const val LAST_VISITED = "last_visited"
        const val LAST_VISITED_ITEM_ID = "last_visited_item_id"
    }
}