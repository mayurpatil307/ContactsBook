package com.example.contactsbook.ui.calls.outgoingcalls

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contactsbook.R
import com.example.contactsbook.ui.calls.outgoingcalls.OutgoingCallRecyclerViewAdapter
import com.example.contactsbook.ui.calls.placeholder.PlaceholderContent

class OutgoingCallsFragment : Fragment() {

    private var adapter: OutgoingCallRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_outgoing_calls_list, container, false)

        adapter = OutgoingCallRecyclerViewAdapter(PlaceholderContent.ITEMS)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = this@OutgoingCallsFragment.adapter
            }
        }
        return view
    }
}