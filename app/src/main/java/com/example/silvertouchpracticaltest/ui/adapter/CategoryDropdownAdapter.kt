package com.example.silvertouchpracticaltest.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.silvertouchpracticaltest.database.entity.Category
import com.example.silvertouchpracticaltest.databinding.CategoryDropdownItemBinding

class CategoryDropdownAdapter(
    context: Context,
    private val layoutResourceId: Int,
    private val categories: MutableList<Category>
) :
    ArrayAdapter<Category>(context, layoutResourceId, categories) {


    override fun getCount(): Int = categories.size

    override fun getItem(position: Int): Category = categories[position]

    override fun getItemId(position: Int): Long = categories[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CategoryDropdownItemBinding

        val view: View = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = CategoryDropdownItemBinding.inflate(inflater, parent, false)
            binding.root
        } else {
            binding = DataBindingUtil.getBinding(convertView)!!
            binding.root
        }

        val item = getItem(position)
        binding.category = item

        return view
    }

}