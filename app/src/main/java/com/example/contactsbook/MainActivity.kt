package com.example.contactsbook

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.contactsbook.databinding.ActivityMainBinding
import com.example.contactsbook.extensions.registerRequestLauncher
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var activityRequestLauncher: ActivityResultLauncher<String>

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

        binding.appBarMain.ivRefresh.setOnClickListener {
            Toast.makeText(this, "Data Refreshed", Toast.LENGTH_SHORT).show()
            viewModel.emitRefreshEvent(true)
        }

        binding.navView.setNavigationItemSelectedListener(this)

        activityRequestLauncher = registerRequestLauncher {
            viewModel.updatePermissionResult(it)
        }

        viewModel.showToastEvent.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        val lastVisitedItemId = viewModel.getLastVisitedItemId()
        if (R.id.nav_contacts != lastVisitedItemId) findNavController(R.id.nav_host_fragment_content_main).navigate(
            lastVisitedItemId
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getActivityRequestLauncher(): ActivityResultLauncher<String> {
        return activityRequestLauncher
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (item.title?.equals(CONTACTS) == true) {
            navController.navigate(R.id.nav_contacts)
        } else if (item.title?.equals(CALL_LOGS) == true) {
            navController.navigate(R.id.nav_call_logs)
        } else if (item.title?.equals(SMS) == true) {
            navController.navigate(R.id.nav_sms_inbox)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true
    }

    companion object {
        val CONTACTS = "Contacts"
        val CALL_LOGS = "Call Logs"
        val SMS = "SMS Inbox"
    }

}