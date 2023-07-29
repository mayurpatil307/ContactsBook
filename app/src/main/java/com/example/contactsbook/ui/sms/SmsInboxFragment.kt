package com.example.contactsbook.ui.sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.MainActivity
import com.example.contactsbook.databinding.FragmentSmsInboxListBinding

class SmsInboxFragment : Fragment() {
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
        smsListAdapter = SmsListAdapter()

        val isReadSmsAllowed = (activity as MainActivity).isSmsPermGranted

        binding.rvSmsInbox.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = smsListAdapter
        }

        viewModel.smsList.observe(viewLifecycleOwner) { messages ->
            smsListAdapter.submitList(messages)
        }

        if (isReadSmsAllowed == true) {
            viewModel.loadSMSMessages()
        } else {
            Toast.makeText(
                requireActivity(),
                "Read Permissions for SMS has not been granted by the user.",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}