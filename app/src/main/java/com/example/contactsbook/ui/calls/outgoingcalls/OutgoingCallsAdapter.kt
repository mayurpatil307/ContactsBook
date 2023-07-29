package com.example.contactsbook.ui.calls.outgoingcalls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsbook.R
import com.example.contactsbook.databinding.ItemIncomingCallBinding
import com.example.contactsbook.models.CallLogItem

class OutgoingCallsAdapter :
    ListAdapter<CallLogItem, OutgoingCallsAdapter.ViewHolder>(IncomingCallsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIncomingCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemIncomingCallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CallLogItem) {
            binding.callerNameTextView.text = item.callerName ?: "Unknown"
            binding.callerNumberTextView.text = item.callerNumber
            binding.callTypeImageView.setImageResource(R.drawable.ic_outgoing_call)
        }
    }

    class IncomingCallsDiffCallback : DiffUtil.ItemCallback<CallLogItem>() {
        override fun areItemsTheSame(oldItem: CallLogItem, newItem: CallLogItem): Boolean {
            return oldItem.callerNumber == newItem.callerNumber
        }

        override fun areContentsTheSame(oldItem: CallLogItem, newItem: CallLogItem): Boolean {
            return oldItem == newItem
        }
    }
}
