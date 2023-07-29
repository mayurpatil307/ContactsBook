package com.example.contactsbook

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.contactsbook.databinding.ActivityMainBinding
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.extensions.registerRequestLauncher

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val smsPermission = Manifest.permission.READ_SMS
    private val contactPermission = Manifest.permission.READ_CONTACTS

    private val sharedPrefs by lazy {
        getSharedPreferences("last_visited", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_contacts, R.id.nav_call_logs, R.id.nav_sms_inbox
            ), drawerLayout
        )

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Check if there is a stored last visited item
        val lastVisitedItemId = getLastVisitedItemId()
        if (lastVisitedItemId != R.id.nav_contacts) {
            navController.navigate(lastVisitedItemId)
            Log.d("TAGGU", "not contacts: $lastVisitedItemId")
        } else {
            //default -> contacts
            navController.navigate(R.id.nav_contacts)
            Log.d("TAGGU", "contacts: $lastVisitedItemId")
        }
        Log.d("TAGGU", "nav_sms_inbox: $lastVisitedItemId")

        requestPermissions()
    }

    private fun getLastVisitedItemId(): Int {
        val sharedPrefs = getSharedPreferences("last_visited", Context.MODE_PRIVATE)
        return sharedPrefs.getInt("last_visited_item_id", R.id.nav_contacts)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (!isPermissionIsGranted(smsPermission)) {
            permissionsToRequest.add(smsPermission)
        }
        if (!isPermissionIsGranted(contactPermission)) {
            permissionsToRequest.add(contactPermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Closing the app, since the permissions are necessary.", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
            }

            viewModel.updatePermissionStatus(true)

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}