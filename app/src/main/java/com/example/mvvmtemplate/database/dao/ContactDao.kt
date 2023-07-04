package com.example.mvvmtemplate.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mvvmtemplate.database.entity.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact (contact: Contact)

    @Update
    suspend fun updateContact (contact: Contact)

    @Delete
    suspend  fun deleteContact (contact: Contact)

    @Query("SELECT * FROM contact")
    fun getContacts () : LiveData<List<Contact>>


    @Query("SELECT * FROM contact where id = :contactId")
    suspend fun getContactById(contactId: Long): Contact?
}