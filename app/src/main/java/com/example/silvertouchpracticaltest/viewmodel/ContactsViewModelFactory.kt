package com.example.silvertouchpracticaltest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.silvertouchpracticaltest.repository.ContactRepository

class ContactsViewModelFactory(private val repository: ContactRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactViewModel(repository) as T
    }
}