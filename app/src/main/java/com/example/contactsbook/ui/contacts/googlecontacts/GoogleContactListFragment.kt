package com.example.contactsbook.ui.contacts.googlecontacts

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.contactsbook.R
import com.example.contactsbook.databinding.FragmentGoogleContactsListBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.api.services.people.v1.PeopleScopes

class GoogleContactListFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {
    var mGoogleApiClient: GoogleApiClient? = null
    private val viewModel: GoogleAuthViewModel by viewModels()
    val RC_INTENT = 200
    val RC_API_CHECK = 100
    private lateinit var binding: FragmentGoogleContactsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) // The serverClientId is an OAuth 2.0 web client ID
                .requestServerAuthCode(getString(R.string.clientID))
                .requestEmail()
                .requestScopes(
                    Scope(Scopes.PLUS_LOGIN),
                    Scope(PeopleScopes.CONTACTS_READONLY),
                    Scope(PeopleScopes.USER_EMAILS_READ),
                    Scope(PeopleScopes.USERINFO_EMAIL),
                    Scope(PeopleScopes.USER_PHONENUMBERS_READ)
                )
                .build()


        // To connect with Google Play Services and Sign In
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .enableAutoManage(requireActivity(), this)
            .addOnConnectionFailedListener(this)
            .addConnectionCallbacks(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoogleContactsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener {
            idToken
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    private val idToken: Unit
        private get() {
            // Show an account picker to let the user choose a Google account from the device.
            // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
            // consent screen will be shown here.
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient!!)
            startActivityForResult(signInIntent, RC_INTENT)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_INTENT -> {
                Log.d("TAGGU", "sign in result")
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
                if (result!!.isSuccess) {
                    val acct = result.signInAccount
                    Log.d("TAGGU", "onActivityResult:GET_TOKEN:success:" + result.status.isSuccess)
                    // This is what we need to exchange with the server.
                    Log.d("TAGGU", "auth Code:" + acct!!.serverAuthCode)
                    //PeoplesAsync().execute(acct.serverAuthCode)
                } else {
                    Log.d("TAGGU", "sign in fail" + result.status)
                }
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        val mGoogleApiAvailability = GoogleApiAvailability.getInstance()
        val dialog: Dialog? =
            mGoogleApiAvailability.getErrorDialog(this, connectionResult.errorCode, RC_API_CHECK)
        dialog?.show()
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }
}