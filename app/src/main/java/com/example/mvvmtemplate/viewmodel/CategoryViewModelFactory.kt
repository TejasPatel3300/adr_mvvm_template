package com.example.mvvmtemplate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmtemplate.repository.CategoryRepository

class CategoryViewModelFactory(private val repository: CategoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(repository) as T
    }
}