package com.example.mvvmtemplate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtemplate.R
import com.example.mvvmtemplate.database.AppDatabase
import com.example.mvvmtemplate.database.entity.Category
import com.example.mvvmtemplate.databinding.FragmentCategoriesBinding
import com.example.mvvmtemplate.repository.CategoryRepository
import com.example.mvvmtemplate.ui.adapter.CategoriesAdapter
import com.example.mvvmtemplate.utils.hideKeyboard
import com.example.mvvmtemplate.viewmodel.CategoryViewModel
import com.example.mvvmtemplate.viewmodel.CategoryViewModelFactory


class CategoriesFragment : Fragment() {
    private var binding: FragmentCategoriesBinding? = null

    private var categoriesAdapter: CategoriesAdapter? = null
    private var categoriesRV: RecyclerView? = null

    private var addCategoryET: EditText? = null
    private var btnSave: Button? = null

    private val onItemActionListener: CategoriesAdapter.OnItemActionListener = object :CategoriesAdapter.OnItemActionListener{
        override fun onItemDeleteClick(item: Category?) {
            item?.let {
                viewModel.deleteCategory(item)
            }
        }

        override fun onItemEditClick(item: Category?) {
            // todo: determine how we will create update category flow
        }
    }

    private val viewModel: CategoryViewModel by lazy {
        ViewModelProvider(requireActivity(), CategoryViewModelFactory(CategoryRepository(AppDatabase.getDatabase(requireActivity())))).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        initializeData()
    }

    private fun initializeData() {
        viewModel.categories.observe(viewLifecycleOwner) {categories ->
            categoriesAdapter?.updateData(categories)
        }
    }

    private fun setUpViews() {
        binding?.let { binding ->

            addCategoryET = binding.etAddCategory
            btnSave = binding.btnSave

            categoriesAdapter = CategoriesAdapter(onItemActionListener)

            categoriesRV = binding.rvCategories
            categoriesRV?.apply {
                adapter = categoriesAdapter
                layoutManager = LinearLayoutManager(this@CategoriesFragment.context)
            }

            btnSave?.setOnClickListener {
                val categoryText =  addCategoryET?.text?.toString()
                if (!categoryText.isNullOrEmpty()) {
                    val category = Category(name = categoryText)
                    hideKeyboard(requireActivity())
                    addCategoryET?.text?.clear()
                    viewModel.insertCategory(category)
                }
            }
        }
    }

}