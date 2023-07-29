package com.example.contactsbook.ui.calls.missedcalls

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
import com.example.contactsbook.databinding.FragmentMissedCallsListBinding

class MissedCallsFragment : Fragment() {

    private lateinit var binding: FragmentMissedCallsListBinding
    private lateinit var missedCallsAdapter: MissedCallsAdapter
    private val viewModel: MissedCallsViewModel by viewModels()

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

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = missedCallsAdapter
        }

        viewModel.missedCallsList.observe(viewLifecycleOwner) { missedCalls ->
            missedCallsAdapter.submitList(missedCalls.toMutableList())
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchMissedCallsList(requireContext())
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
                viewModel.fetchMissedCallsList(requireContext())
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1003
    }
}