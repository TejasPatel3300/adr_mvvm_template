package com.example.silvertouchpracticaltest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.silvertouchpracticaltest.database.entity.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory (category: Category)

    @Update
    suspend fun updateCategory (category: Category)

    @Delete
    suspend fun deleteCategory (category: Category)

    @Query("SELECT * FROM category")
    fun getCategories () : LiveData<List<Category>>
}