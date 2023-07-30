package com.example.contactsbook.ui.contacts.localcontacts

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.contactsbook.MainActivity
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.R
import com.example.contactsbook.databinding.FragmentLocalContactsListBinding
import com.example.contactsbook.dialogs.ContactActionDialogFragment
import com.example.contactsbook.extensions.isPermissionIsGranted
import com.example.contactsbook.extensions.registerRequestLauncher
import com.example.contactsbook.models.Contact
import com.example.contactsbook.ui.contacts.localcontacts.placeholder.PlaceholderContent
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
            binding.swipeRefreshContacts.isRefreshing = true
            viewModel.loadContacts()
        }

        if((requireActivity() as MainActivity).isPermissionIsGranted(Manifest.permission.READ_CONTACTS)){
            binding.swipeRefreshContacts.isRefreshing = true
            viewModel.loadContacts()
        }

        initObservers()
    }

    private fun initObservers() {
        parentViewModel.isPermissionsGranted.observe(viewLifecycleOwner){
            if (it){
                viewModel.loadContacts()
            }
        }
    }

    private fun showContactActionDialog(contact: Contact) {
        val contactName = contact.name
        val contactNumber = contact.phoneNumber
        val dialogFragment = ContactActionDialogFragment.newInstance(contactName, contactNumber)
        dialogFragment.show(parentFragmentManager, "contact_action_dialog")
    }

}
