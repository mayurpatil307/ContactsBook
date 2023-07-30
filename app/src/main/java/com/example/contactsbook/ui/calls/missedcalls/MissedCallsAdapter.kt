package com.example.contactsbook.ui.calls.missedcalls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsbook.R
import com.example.contactsbook.databinding.ItemIncomingCallBinding
import com.example.contactsbook.models.CallLogItem


class MissedCallsAdapter :
    ListAdapter<CallLogItem, MissedCallsAdapter.ViewHolder>(MissedCallsDiffCallback()) {

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
            binding.callTypeImageView.setImageResource(R.drawable.ic_missed_call)
            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    class MissedCallsDiffCallback : DiffUtil.ItemCallback<CallLogItem>() {
        override fun areItemsTheSame(oldItem: CallLogItem, newItem: CallLogItem): Boolean {
            return oldItem.callerNumber == newItem.callerNumber
        }

        override fun areContentsTheSame(oldItem: CallLogItem, newItem: CallLogItem): Boolean {
            return oldItem == newItem
        }
    }
}
