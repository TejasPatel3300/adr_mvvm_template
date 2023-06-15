package com.example.silvertouchpracticaltest.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.silvertouchpracticaltest.database.entity.Category
import com.example.silvertouchpracticaltest.database.entity.Contact

@Dao
interface ContactDao {

    @Insert
    suspend fun insertContact (contact: Contact)

    @Update
    suspend fun updateContact (contact: Contact)

    @Delete
    suspend  fun deleteContact (contact: Contact)

    @Query("SELECT * FROM contact")
    fun getContacts () : LiveData<List<Contact>>
}