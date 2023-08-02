package com.example.contactsbook.ui.calls.incomingcalls

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
import com.example.contactsbook.databinding.FragmentIncomingCallsListBinding
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class IncomingCallsFragment : Fragment() {

    private lateinit var binding: FragmentIncomingCallsListBinding
    private lateinit var incomingCallsListAdapter: IncomingCallsListAdapter
    private val viewModel: IncomingCallsViewModel by viewModels()

    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomingCallsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomingCallsListAdapter = IncomingCallsListAdapter()
        incomingCallsListAdapter.onItemClick = { callLogItem ->
            showDialer(callLogItem)
        }
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incomingCallsListAdapter
        }

        viewModel.incomingCallsList.observe(viewLifecycleOwner) { incomingCalls ->
            binding.swipeRefreshIncoming.isRefreshing = false
            incomingCallsListAdapter.submitList(incomingCalls.toMutableList())
        }

        binding.swipeRefreshIncoming.setOnRefreshListener {
            Toast.makeText(requireContext(), TOAST_DATA_REFRESHED, Toast.LENGTH_SHORT).show()
            binding.swipeRefreshIncoming.isRefreshing = true
            viewModel.fetchIncomingCalls()
        }

        lifecycleScope.launch {
            parentViewModel.permissionLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    binding.swipeRefreshIncoming.isRefreshing = true
                    viewModel.fetchIncomingCalls()
                } else {
                    parentViewModel.emitToastEvent(TOAST_CALL_LOGS)
                }
            }
        }

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                binding.swipeRefreshIncoming.isRefreshing = true
                viewModel.fetchIncomingCalls()
            }
        }

        checkCallsPermission()
    }

    private fun checkCallsPermission() {
        if ((requireActivity() as MainActivity).isPermissionIsGranted(Manifest.permission.READ_CALL_LOG)) {
            binding.swipeRefreshIncoming.isRefreshing = true
            viewModel.fetchIncomingCalls()
        } else {
            (requireActivity() as MainActivity).getActivityRequestLauncher()
                .launch(Manifest.permission.READ_CALL_LOG)
        }
    }

    private fun showDialer(callLogItem: CallLogItem) {
        val phoneNumber = callLogItem.callerNumber
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }

    companion object {
        const val TOAST_CALL_LOGS = "Reading Call Logs Permission is denied by user"
        const val TOAST_DATA_REFRESHED = "Data Refreshed"
    }
}
