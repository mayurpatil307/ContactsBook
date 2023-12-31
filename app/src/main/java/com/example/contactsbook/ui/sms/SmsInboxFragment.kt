package com.example.contactsbook.ui.sms

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.MainActivity
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.R
import com.example.contactsbook.databinding.FragmentSmsInboxListBinding
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.extensions.toPresentableTime
import com.example.contactsbook.models.SMSMessage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SmsInboxFragment : Fragment(), SmsItemClickListener {
    private lateinit var viewModel: SmsViewModel
    private lateinit var smsListAdapter: SmsListAdapter
    private lateinit var binding: FragmentSmsInboxListBinding

    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SmsViewModel::class.java)
        binding = FragmentSmsInboxListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smsListAdapter = SmsListAdapter(this)

        binding.rvSmsInbox.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = smsListAdapter
        }

        viewModel.smsList.observe(viewLifecycleOwner) { messages ->
            binding.swipeRefreshSms.isRefreshing = false
            smsListAdapter.submitList(messages)
        }

        binding.swipeRefreshSms.setOnRefreshListener {
            Toast.makeText(requireContext(), TOAST_DATA_REFRESHED, Toast.LENGTH_SHORT).show()
            binding.swipeRefreshSms.isRefreshing = true
            viewModel.loadSMSMessages()
        }

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                if (it) {
                    binding.swipeRefreshSms.isRefreshing = true
                    viewModel.loadSMSMessages()
                } else {
                    parentViewModel.showToastEvent.postValue(TOAST_SMS)
                }
            }
        }

        lifecycleScope.launch {
            parentViewModel.permissionLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    binding.swipeRefreshSms.isRefreshing = true
                    viewModel.loadSMSMessages()
                } else {
                    parentViewModel.showToastEvent.postValue(TOAST_SMS)
                }
            }
        }

        checkSmsPermission()

        parentViewModel.saveLastVisitedItemId(R.id.nav_sms_inbox)
    }

    private fun checkSmsPermission() {
        if ((requireActivity() as MainActivity).isPermissionIsGranted(Manifest.permission.READ_SMS)) {
            binding.swipeRefreshSms.isRefreshing = true
            viewModel.loadSMSMessages()
        } else {
            (requireActivity() as MainActivity).getActivityRequestLauncher()
                .launch(Manifest.permission.READ_SMS)
        }
    }

    override fun onSmsItemClick(item: SMSMessage) {
        showSmsDetailDialog(item.sender, item.body, item.timestamp)
    }

    private fun showSmsDetailDialog(sender: String, body: String, time: Long) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.sms_dialog, null)

        dialogView.findViewById<TextView>(R.id.dialogSenderTextView).text = sender
        dialogView.findViewById<TextView>(R.id.dialogBodyTextView).text = body
        dialogView.findViewById<TextView>(R.id.timeTextView).text = time.toPresentableTime()

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.dialogCloseButton).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }

    companion object {
        val TOAST_SMS = "Reading SMS Permission is denied by user"
        val TOAST_DATA_REFRESHED = "Data Refreshed"
    }

}