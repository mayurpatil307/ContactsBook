package com.example.contactsbook.ui.calls.missedcalls

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.MainActivity
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.databinding.FragmentMissedCallsListBinding
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.models.CallLogItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MissedCallsFragment : Fragment() {

    private lateinit var binding: FragmentMissedCallsListBinding
    private lateinit var missedCallsAdapter: MissedCallsAdapter
    private val viewModel: MissedCallsViewModel by viewModels()

    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMissedCallsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        missedCallsAdapter = MissedCallsAdapter()
        missedCallsAdapter.onItemClick = { callLogItem ->
            showDialer(callLogItem)
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = missedCallsAdapter
        }

        viewModel.missedCallsList.observe(viewLifecycleOwner) { missedCalls ->
            binding.swipeRefreshMissed.isRefreshing = false
            missedCallsAdapter.submitList(missedCalls.toMutableList())
            if (missedCalls.toMutableList().size > 3) {
                binding.list.smoothScrollToPosition(4)
            }

        }

        binding.swipeRefreshMissed.setOnRefreshListener {
            binding.swipeRefreshMissed.isRefreshing = true
            viewModel.fetchMissedCallsList()
        }

        lifecycleScope.launch {
            parentViewModel.permissionLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    binding.swipeRefreshMissed.isRefreshing = true
                    viewModel.fetchMissedCallsList()
                } else {
                    parentViewModel.emitToastEvent(TOAST_CALL_LOGS)
                }
            }
        }

        checkCallsPermission()

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                binding.swipeRefreshMissed.isRefreshing = true
                viewModel.fetchMissedCallsList()
            }
        }
    }

    private fun showDialer(callLogItem: CallLogItem) {
        val phoneNumber = callLogItem.callerNumber
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }


    private fun checkCallsPermission() {
        if ((requireActivity() as MainActivity).isPermissionIsGranted(Manifest.permission.READ_CALL_LOG)) {
            binding.swipeRefreshMissed.isRefreshing = true
            viewModel.fetchMissedCallsList()
        } else {
            (requireActivity() as MainActivity).getActivityRequestLauncher()
                .launch(Manifest.permission.READ_CALL_LOG)
        }
    }

    companion object {
        const val TOAST_CALL_LOGS = "Reading Call Logs Permission is denied by user"
    }
}