package com.example.contactsbook.ui.calls.missedcalls

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contactsbook.R
import com.example.contactsbook.ui.calls.missedcalls.placeholder.PlaceholderContent

class MissedCallsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_missed_calls_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager =  LinearLayoutManager(context)
                adapter = MissedCallsAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }
}