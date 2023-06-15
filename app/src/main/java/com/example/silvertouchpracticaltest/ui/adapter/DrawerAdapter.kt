package com.example.silvertouchpracticaltest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.silvertouchpracticaltest.R
import com.example.silvertouchpracticaltest.databinding.DrawerNavItemBinding


class DrawerAdapter(private val drawerMenuTitles: List<String>, val listener: OnItemClickListener): RecyclerView.Adapter<DrawerAdapter.DrawerItemViewHolder>() {


    inner class DrawerItemViewHolder(private val binding: DrawerNavItemBinding) : ViewHolder(binding.root) {

        fun bind(item: String?, itemIndex: Int, listener: OnItemClickListener) {
            binding.menuTitle = item
            binding.menuItemIndex = itemIndex
            binding.onItemClickListener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: DrawerNavItemBinding = DataBindingUtil.inflate(inflater, R.layout.drawer_nav_item, parent, false)
        return DrawerItemViewHolder(binding)
    }

    override fun getItemCount(): Int = drawerMenuTitles.size

    override fun onBindViewHolder(holder: DrawerItemViewHolder, position: Int) {
        holder.bind(drawerMenuTitles[position], position,listener)
    }


    interface OnItemClickListener {
        fun onItemClick(drawerMenuItemIndex: Int?)
    }
}