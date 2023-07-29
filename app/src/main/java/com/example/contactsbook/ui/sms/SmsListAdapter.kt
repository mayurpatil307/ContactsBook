package com.example.contactsbook.ui.sms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsbook.databinding.ItemSmsInboxBinding
import com.example.contactsbook.models.SMSMessage

class SmsListAdapter :
    ListAdapter<SMSMessage, SmsListAdapter.ViewHolder>(SmsMessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSmsInboxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemSmsInboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SMSMessage) {
            binding.smsSenderTextView.text = item.sender
            binding.smsBodyTextView.text = item.body
        }
    }

    class SmsMessageDiffCallback : DiffUtil.ItemCallback<SMSMessage>() {
        override fun areItemsTheSame(oldItem: SMSMessage, newItem: SMSMessage): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: SMSMessage, newItem: SMSMessage): Boolean {
            return oldItem == newItem
        }
    }
}
