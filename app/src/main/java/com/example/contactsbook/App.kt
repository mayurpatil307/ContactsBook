package com.example.contactsbook

import android.app.Application
import com.google.firebase.FirebaseApp

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        FirebaseApp.initializeApp(this);
    }
}