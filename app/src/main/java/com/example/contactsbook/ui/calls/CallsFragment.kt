package com.example.contactsbook.ui.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.contactsbook.databinding.FragmentCallsBinding
import com.example.contactsbook.ui.calls.incomingcalls.IncomingCallsFragment
import com.example.contactsbook.ui.calls.missedcalls.MissedCallsFragment
import com.example.contactsbook.ui.calls.outgoingcalls.OutgoingCallsFragment
import com.example.contactsbook.ui.common.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class CallsFragment : Fragment() {

    private var _binding: FragmentCallsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callsViewModel =
            ViewModelProvider(this).get(CallsViewModel::class.java)

        _binding = FragmentCallsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPagerAdapter()
    }

    private fun setViewPagerAdapter() {
        val adapter = ViewPagerAdapter(this)

        adapter.addFragment(IncomingCallsFragment())
        adapter.addFragment(OutgoingCallsFragment())
        adapter.addFragment(MissedCallsFragment())

        binding.viewpagerCalls.adapter = adapter
        binding.viewpagerCalls.requestDisallowInterceptTouchEvent(true)
        binding.viewpagerCalls.isUserInputEnabled = false
        binding.viewpagerCalls.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        TabLayoutMediator(
            binding.callsTabLayout,
            binding.viewpagerCalls
        ) { tab, position ->
            if (position == POSITION_ZERO) tab.text = INCOMING_CALLS_STRING
            else if (position == POSITION_ONE) tab.text = OUTGOING_CALLS_STRING
            else if (position == POSITION_TWO) tab.text = MISSED_CALLS_STRING
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val INCOMING_CALLS_STRING = "Incoming Calls"
        private const val OUTGOING_CALLS_STRING = "Outgoing Calls"
        private const val MISSED_CALLS_STRING = "Missed Calls"

        const val POSITION_ZERO = 0
        const val POSITION_ONE = 1
        const val POSITION_TWO = 2
    }
}