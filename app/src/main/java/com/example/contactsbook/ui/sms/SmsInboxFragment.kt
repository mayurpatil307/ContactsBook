package com.example.contactsbook.ui.sms

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.MainActivity
import com.example.contactsbook.R
import com.example.contactsbook.databinding.FragmentSmsInboxListBinding
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.extensions.toPresentableTime
import com.example.contactsbook.models.SMSMessage

class SmsInboxFragment : Fragment(), SmsItemClickListener {
    private lateinit var viewModel: SmsViewModel
    private lateinit var smsListAdapter: SmsListAdapter
    private lateinit var binding: FragmentSmsInboxListBinding

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

        val isReadSmsAllowed = (activity as MainActivity).isPermissionIsGranted(Manifest.permission.READ_SMS)

        binding.rvSmsInbox.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = smsListAdapter
        }

        viewModel.smsList.observe(viewLifecycleOwner) { messages ->
            binding.swipeRefreshSms.isRefreshing = false
            smsListAdapter.submitList(messages)
        }

        binding.swipeRefreshSms.setOnRefreshListener {
            binding.swipeRefreshSms.isRefreshing = true
            viewModel.loadSMSMessages()
        }

        if (isReadSmsAllowed) {
            binding.swipeRefreshSms.isRefreshing = true
            viewModel.loadSMSMessages()
        } else {
            Toast.makeText(
                requireActivity(),
                "Read Permissions for SMS has not been granted by the user.",
                Toast.LENGTH_LONG
            ).show()
        }

        saveLastVisitedItemId(R.id.nav_sms_inbox)
    }

    private fun saveLastVisitedItemId(itemId: Int) {
        val sharedPrefs = requireActivity().getSharedPreferences("last_visited", Context.MODE_PRIVATE)
        sharedPrefs.edit().putInt("last_visited_item_id", itemId).apply()
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

}