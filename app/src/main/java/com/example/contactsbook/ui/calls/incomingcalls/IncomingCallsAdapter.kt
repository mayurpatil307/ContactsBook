package com.example.contactsbook.ui.calls.incomingcalls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsbook.R
import com.example.contactsbook.databinding.ItemIncomingCallBinding
import com.example.contactsbook.models.CallLogItem
import com.example.contactsbook.models.Contact

class IncomingCallsListAdapter :
    ListAdapter<CallLogItem, IncomingCallsListAdapter.ViewHolder>(IncomingCallsDiffCallback()) {

    var onItemClick: ((CallLogItem) -> Unit)? = null

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
            binding.callTypeImageView.setImageResource(R.drawable.ic_incoming_call)
            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
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
