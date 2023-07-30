package com.example.contactsbook.ui.calls.outgoingcalls

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.databinding.FragmentOutgoingCallsListBinding
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
            Toast.makeText(requireContext(), "Data Refreshed", Toast.LENGTH_SHORT).show()
            binding.swipeRefreshOutgoing.isRefreshing = true
            viewModel.fetchOutgoingCallsList()
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.swipeRefreshOutgoing.isRefreshing = true
            viewModel.fetchOutgoingCallsList()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CALL_LOG),
                PERMISSION_REQUEST_CODE
            )
        }

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                binding.swipeRefreshOutgoing.isRefreshing = true
                viewModel.fetchOutgoingCallsList()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.swipeRefreshOutgoing.isRefreshing = true
                viewModel.fetchOutgoingCallsList()
            }
        }
    }

    private fun showDialer(callLogItem: CallLogItem) {
        val phoneNumber = callLogItem.callerNumber
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1003
    }
}