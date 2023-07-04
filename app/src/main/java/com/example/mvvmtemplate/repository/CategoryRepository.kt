package com.example.mvvmtemplate.repository

import androidx.lifecycle.LiveData
import com.example.mvvmtemplate.database.AppDatabase
import com.example.mvvmtemplate.database.entity.Category

class CategoryRepository(private val database: AppDatabase) {

    private val dao = database.getCategoryDao()

    suspend fun insertCategory (category: Category) = dao.insertCategory(category = category)

    suspend fun updateCategory (category: Category) = dao.updateCategory(category = category)

    suspend fun deleteCategory (category: Category) = dao.deleteCategory(category = category)

    fun getCategories () : LiveData<List<Category>> = dao.getCategories()
}