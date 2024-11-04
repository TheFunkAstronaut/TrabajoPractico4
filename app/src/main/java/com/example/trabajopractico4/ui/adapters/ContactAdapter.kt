package com.example.trabajopractico4.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trabajopractico4.databinding.ItemContactBinding
import com.example.trabajopractico4.models.Contact

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    private var onContactClickListener: ((Contact) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)

        holder.itemView.setOnClickListener {
            onContactClickListener?.invoke(contact)  // Trigger the click listener
        }
    }

    fun setOnContactClickListener(listener: (Contact) -> Unit) {
        onContactClickListener = listener
    }

    class ContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.textViewName.text = "${contact.name} ${contact.last_name}"
            binding.textViewPhone.text = contact.phones.firstOrNull()?.number ?: "No phone"
            // Set up profile picture loading if desired
        }
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
}