package com.example.silvertouchpracticaltest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.silvertouchpracticaltest.R
import com.example.silvertouchpracticaltest.database.entity.Category
import com.example.silvertouchpracticaltest.database.entity.Contact
import com.example.silvertouchpracticaltest.databinding.CategoryItemBinding
import com.example.silvertouchpracticaltest.databinding.ContactItemBinding

class ContactListAdapter(private val itemActionListener: OnItemActionListener): RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {

    private val contacts: MutableList<Contact> = mutableListOf()

    inner class ContactListViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, itemActionListener : OnItemActionListener){
            binding.contact = contact
            binding.onItemActionListener = itemActionListener
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ContactItemBinding =  DataBindingUtil.inflate(inflater, R.layout.contact_item, parent, false)
        return ContactListViewHolder(binding)

    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bind(contacts[position], itemActionListener)
    }


    interface OnItemActionListener {
        fun onItemDeleteClick(item: Contact?)
        fun onItemEditClick(item: Contact?)
    }

    fun updateData(updatedContactsList: List<Contact>) {
        contacts.clear()
        contacts.addAll(updatedContactsList)
        notifyDataSetChanged()
    }
}