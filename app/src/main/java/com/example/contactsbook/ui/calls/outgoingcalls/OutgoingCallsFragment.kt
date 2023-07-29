package com.example.contactsbook.ui.calls.outgoingcalls

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.contactsbook.databinding.FragmentOutgoingCallsListBinding

class OutgoingCallsFragment : Fragment() {

    private lateinit var binding: FragmentOutgoingCallsListBinding
    private lateinit var outgoingCallsAdapter: OutgoingCallsAdapter
    private val viewModel: OutgoingCallsViewModel by viewModels()

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

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = outgoingCallsAdapter
        }

        viewModel.outgoingCallsList.observe(viewLifecycleOwner) { outgoingCalls ->
            outgoingCallsAdapter.submitList(outgoingCalls.toMutableList())
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchOutgoingCallsList(requireContext())
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
                viewModel.fetchOutgoingCallsList(requireContext())
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1003
    }
}