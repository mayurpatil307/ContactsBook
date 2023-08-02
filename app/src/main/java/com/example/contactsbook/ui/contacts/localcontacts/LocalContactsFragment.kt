package com.example.contactsbook.ui.contacts.localcontacts

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsbook.MainActivity
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.R
import com.example.contactsbook.databinding.FragmentLocalContactsListBinding
import com.example.contactsbook.dialogs.ContactActionDialogFragment
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.models.Contact
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LocalContactsFragment : Fragment() {
    private lateinit var viewModel: ContactsViewModel
    private lateinit var contactsListAdapter: ContactsListAdapter
    private lateinit var binding: FragmentLocalContactsListBinding

    private val parentViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        binding = FragmentLocalContactsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactsListAdapter = ContactsListAdapter(mutableListOf())
        contactsListAdapter.onItemClick = { contact ->
            showContactActionDialog(contact)
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsListAdapter
        }

        viewModel.contactsList.observe(viewLifecycleOwner) { contacts ->
            binding.swipeRefreshContacts.isRefreshing = false
            contactsListAdapter.updateData(contacts.toMutableList())
        }

        binding.swipeRefreshContacts.setOnRefreshListener {
            checkContactsPermission()
        }

        initObservers()
        checkContactsPermission()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            parentViewModel.permissionLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    binding.swipeRefreshContacts.isRefreshing = true
                    viewModel.loadContacts()
                } else {
                    parentViewModel.emitToastEvent(TOAST_CONTACT)
                }
            }
        }

        lifecycleScope.launch {
            parentViewModel.refreshEventSharedFlow.collectLatest {
                binding.swipeRefreshContacts.isRefreshing = true
                viewModel.loadContacts()
            }
        }

        lifecycleScope.launch {
            parentViewModel.permissionLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    binding.swipeRefreshContacts.isRefreshing = true
                    viewModel.loadContacts()
                } else {
                    parentViewModel.emitToastEvent(TOAST_CONTACT)
                }
            }
        }

        parentViewModel.saveLastVisitedItemId(R.id.nav_contacts)
    }

    private fun checkContactsPermission() {
        if ((requireActivity() as MainActivity).isPermissionIsGranted(Manifest.permission.READ_CONTACTS)) {
            binding.swipeRefreshContacts.isRefreshing = true
            viewModel.loadContacts()
        } else {
            (requireActivity() as MainActivity).getActivityRequestLauncher()
                .launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun showContactActionDialog(contact: Contact) {
        val contactName = contact.name
        val contactNumber = contact.phoneNumber
        val dialogFragment = ContactActionDialogFragment.newInstance(contactName, contactNumber)
        dialogFragment.show(parentFragmentManager, "contact_action_dialog")
    }

    companion object {
        const val TOAST_CONTACT = "Access Contacts Permission is denied by user"
    }

}
