package com.example.contactsbook.ui.calls.outgoingcalls

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.MainActivity
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.databinding.FragmentOutgoingCallsListBinding
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OutgoingCallsFragment : Fragment() {

    private lateinit var binding: FragmentOutgoingCallsListBinding
    private lateinit var outgoingCallsAdapter: OutgoingCallsAdapter
    private val viewModel: OutgoingCallsViewModel by viewModels()

    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOutgoingCallsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        outgoingCallsAdapter = OutgoingCallsAdapter()
        outgoingCallsAdapter.onItemClick = { callLogItem ->
            showDialer(callLogItem)
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = outgoingCallsAdapter
        }

        viewModel.outgoingCallsList.observe(viewLifecycleOwner) { outgoingCalls ->
            binding.swipeRefreshOutgoing.isRefreshing = false
            outgoingCallsAdapter.submitList(outgoingCalls.toMutableList())
        }

        binding.swipeRefreshOutgoing.setOnRefreshListener {
            Toast.makeText(requireContext(), TOAST_DATA_REFRESHED, Toast.LENGTH_SHORT).show()
            binding.swipeRefreshOutgoing.isRefreshing = true
            viewModel.fetchOutgoingCallsList()
        }

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                binding.swipeRefreshOutgoing.isRefreshing = true
                viewModel.fetchOutgoingCallsList()
            }
        }

        lifecycleScope.launch {
            parentViewModel.permissionLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    binding.swipeRefreshOutgoing.isRefreshing = true
                    viewModel.fetchOutgoingCallsList()
                } else {
                    parentViewModel.emitToastEvent(TOAST_CALL_LOGS)
                }
            }
        }

        checkCallsPermission()
    }

    private fun showDialer(callLogItem: CallLogItem) {
        val phoneNumber = callLogItem.callerNumber
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }

    private fun checkCallsPermission() {
        if ((requireActivity() as MainActivity).isPermissionIsGranted(Manifest.permission.READ_CALL_LOG)) {
            binding.swipeRefreshOutgoing.isRefreshing = true
            viewModel.fetchOutgoingCallsList()
        } else {
            (requireActivity() as MainActivity).getActivityRequestLauncher()
                .launch(Manifest.permission.READ_CALL_LOG)
        }
    }

    companion object {
        const val TOAST_CALL_LOGS = "Reading Call Logs Permission is denied by user"
        const val TOAST_DATA_REFRESHED = "Data Refreshed"
    }
}