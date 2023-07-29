package com.example.contactsbook.ui.contacts.localcontacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsbook.models.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsViewModel : ViewModel() {

    private val _contactsList = MutableLiveData<List<Contact>>()
    val contactsList: LiveData<List<Contact>> get() = _contactsList

    fun loadContacts(context: Context) {
        viewModelScope.launch {
            val contacts = fetchContacts(context)
            withContext(Dispatchers.Main) {
                _contactsList.value = contacts
            }
        }
    }

    private fun fetchContacts(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0"
        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val nameColumnIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoUriColumnIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (it.moveToNext()) {
                val name = it.getString(nameColumnIndex)
                val phoneNumber = it.getString(numberColumnIndex)
                val photoUri = it.getString(photoUriColumnIndex)
                contacts.add(Contact(name, phoneNumber, photoUri))
            }
        }

        return contacts
    }
}