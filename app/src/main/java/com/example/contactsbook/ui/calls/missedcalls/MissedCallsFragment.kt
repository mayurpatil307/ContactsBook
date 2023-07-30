package com.example.contactsbook.ui.calls.missedcalls

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.databinding.FragmentMissedCallsListBinding
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
        }

        binding.swipeRefreshMissed.setOnRefreshListener {
            binding.swipeRefreshMissed.isRefreshing = true
            viewModel.fetchMissedCallsList()
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.swipeRefreshMissed.isRefreshing = true
            viewModel.fetchMissedCallsList()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CALL_LOG),
                PERMISSION_REQUEST_CODE
            )
        }

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                binding.swipeRefreshMissed.isRefreshing = true
                viewModel.fetchMissedCallsList()
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

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1003
    }
}