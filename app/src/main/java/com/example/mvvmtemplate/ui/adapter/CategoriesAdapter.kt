package com.example.mvvmtemplate.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtemplate.R
import com.example.mvvmtemplate.database.entity.Category
import com.example.mvvmtemplate.databinding.CategoryItemBinding

class CategoriesAdapter(private val itemActionListener : OnItemActionListener): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var categories: MutableList<Category> = mutableListOf()

    inner class CategoriesViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(category: Category, itemActionListener : OnItemActionListener){
            binding.category = category
            binding.onItemActionListener = itemActionListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: CategoryItemBinding =  DataBindingUtil.inflate(inflater, R.layout.category_item, parent, false)
        return CategoriesViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(categories[position], itemActionListener)
    }

    interface OnItemActionListener {
        fun onItemDeleteClick(item: Category?)
        fun onItemEditClick(item: Category?)
    }

    fun updateData(updatedCategories: List<Category>) {
        categories.clear()
        categories.addAll(updatedCategories)
        notifyDataSetChanged()
    }
}