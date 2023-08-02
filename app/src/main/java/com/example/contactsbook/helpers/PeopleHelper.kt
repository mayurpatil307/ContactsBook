package com.example.contactsbook.helpers

import android.content.Context
import com.example.contactsbook.R
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.people.v1.People
import java.io.IOException

object PeopleHelper {
    private const val APPLICATION_NAME = "ContactsBook"

    @Throws(IOException::class)
    fun setUp(context: Context, serverAuthCode: String?): People {
        val httpTransport: HttpTransport = NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()

        // Redirect URL for web based applications.
        // Can be empty too.
        val redirectUrl = "urn:ietf:wg:oauth:2.0:oob"


        // Exchange auth code for access token
        val tokenResponse = GoogleAuthorizationCodeTokenRequest(
            httpTransport,
            jsonFactory,
            context.getString(R.string.clientID),
            context.getString(R.string.clientSecret),
            serverAuthCode,
            redirectUrl
        ).execute()

        // Then, create a GoogleCredential object using the tokens from GoogleTokenResponse
        val credential = GoogleCredential.Builder()
            .setClientSecrets(
                context.getString(R.string.clientID),
                context.getString(R.string.clientSecret)
            )
            .setTransport(httpTransport)
            .setJsonFactory(jsonFactory)
            .build()
        credential.setFromTokenResponse(tokenResponse)

        // credential can then be used to access Google services
        return People.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(APPLICATION_NAME)
            .build()
    }
}