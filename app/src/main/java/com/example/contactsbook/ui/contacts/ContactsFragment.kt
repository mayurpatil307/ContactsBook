package com.example.contactsbook.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.contactsbook.MainViewModel
import com.example.contactsbook.R
import com.example.contactsbook.databinding.FragmentContactsBinding
import com.example.contactsbook.ui.common.ViewPagerAdapter
import com.example.contactsbook.ui.contacts.googlecontacts.GoogleContactsFragment
import com.example.contactsbook.ui.contacts.localcontacts.LocalContactsFragment
import com.google.android.material.tabs.TabLayoutMediator

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPagerAdapter()
    }

    private fun setViewPagerAdapter() {
        val adapter = ViewPagerAdapter(this)

        adapter.addFragment(LocalContactsFragment())
        adapter.addFragment(GoogleContactsFragment())

        binding.viewpagerContacts.adapter = adapter
        binding.viewpagerContacts.requestDisallowInterceptTouchEvent(true)
        binding.viewpagerContacts.isUserInputEnabled = false
        binding.viewpagerContacts.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
        })
        
        TabLayoutMediator(
            binding.contactsTabLayout,
            binding.viewpagerContacts
        ) { tab, position ->
            if (position == POSITION_ZERO) tab.text = LOCAL_CONTACTS_STRING
            else if (position == POSITION_ONE) tab.text = GOOGLE_CONTACTS_STRING
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCAL_CONTACTS_STRING = "Local Contacts"
        private const val GOOGLE_CONTACTS_STRING = "Google Contacts"

        const val POSITION_ZERO = 0
        const val POSITION_ONE = 1
    }
}