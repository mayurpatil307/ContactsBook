package com.example.contactsbook.ui.calls.incomingcalls

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.databinding.FragmentIncomingCallsListBinding
import com.example.contactsbook.databinding.FragmentSmsInboxListBinding
import com.example.contactsbook.ui.sms.SmsViewModel

class IncomingCallsFragment : Fragment() {

    private lateinit var binding: FragmentIncomingCallsListBinding
    private lateinit var incomingCallsListAdapter: IncomingCallsListAdapter
    private val viewModel: IncomingCallsViewModel by viewModels()

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

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = incomingCallsListAdapter
        }

        viewModel.incomingCallsList.observe(viewLifecycleOwner) { incomingCalls ->
            incomingCallsListAdapter.submitList(incomingCalls.toMutableList())
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchIncomingCalls(requireContext())
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CALL_LOG),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.fetchIncomingCalls(requireContext())
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1003
    }
}
