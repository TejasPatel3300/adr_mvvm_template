package com.example.mvvmtemplate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmtemplate.repository.ContactRepository

class ContactsViewModelFactory(private val repository: ContactRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactViewModel(repository) as T
    }
}