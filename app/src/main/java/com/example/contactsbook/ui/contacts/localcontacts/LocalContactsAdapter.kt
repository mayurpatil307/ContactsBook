package com.example.contactsbook.ui.contacts.localcontacts

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.contactsbook.databinding.ItemContactBinding
import com.example.contactsbook.models.Contact

class ContactsListAdapter(private var values: MutableList<Contact>) :
    RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    fun updateData(newData: MutableList<Contact>) {
        values.clear()
        values = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Contact) {
            binding.contactNameTextView.text = item.name
            binding.contactNumberTextView.text = item.phoneNumber

            if (item.photoUri != null) {
                binding.contactImageView.load(Uri.parse(item.photoUri))
            }
        }
    }
}
