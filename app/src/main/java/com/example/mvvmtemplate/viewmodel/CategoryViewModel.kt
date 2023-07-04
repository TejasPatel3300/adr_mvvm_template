package com.example.mvvmtemplate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtemplate.database.entity.Category
import com.example.mvvmtemplate.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _categories: LiveData<List<Category>> = repository.getCategories()
    val categories: LiveData<List<Category>>
        get() = _categories


    fun insertCategory(Category: Category) {
        viewModelScope.launch {
            repository.insertCategory(Category)
        }
    }

    fun updateCategory(Category: Category) {
        viewModelScope.launch {
            repository.updateCategory(Category)
        }
    }

    fun deleteCategory(Category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(Category)
        }
    }
}