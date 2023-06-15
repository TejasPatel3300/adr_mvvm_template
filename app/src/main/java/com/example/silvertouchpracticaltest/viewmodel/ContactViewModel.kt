package com.example.silvertouchpracticaltest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.silvertouchpracticaltest.database.entity.Contact
import com.example.silvertouchpracticaltest.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _validationState: MutableLiveData<ValidationState> =
        MutableLiveData(ValidationState.INITIAL)

    val validationState: LiveData<ValidationState>
        get() = _validationState

    private val _contacts: LiveData<List<Contact>> = repository.getContacts()
    val contacts: LiveData<List<Contact>>
        get() = _contacts

    fun insertContact(contact: Contact) {
        val isValid = validate(contact)
        if (isValid) {
            viewModelScope.launch {
                repository.insertContact(contact)
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun getContacts() {
        viewModelScope.launch {
            repository.getContacts()
        }
    }

    private fun validate(contact: Contact): Boolean {
        val firstName = contact.firstName
        val lastName = contact.lastName
        val email = contact.email
        val mobileNumber = contact.mobileNumber
        val category = contact.category
        val profilePicUri = contact.profilePicUri

        var isValid = true

        if (firstName.isEmpty()) {
            _validationState.value = ValidationState.INVALID_FIRST_NAME
            isValid = false
        } else if (lastName.isEmpty()) {
            _validationState.value = ValidationState.INVALID_LAST_NAME
            isValid = false
        } else if (mobileNumber.isEmpty() || mobileNumber.length != 10) {
            _validationState.value = ValidationState.INVALID_MOBILE
            isValid = false
        } else if (email.isEmpty() || !email.contains('@')) {
            _validationState.value = ValidationState.INVALID_EMAIL
            isValid = false
        } else if (category == -1L) {
            _validationState.value = ValidationState.INVALID_CATEGORY
            isValid = false
        } else if (profilePicUri.isEmpty()) {
            _validationState.value = ValidationState.INVALID_PROFILE_PIC
            isValid = false
        } else {
            _validationState.value = ValidationState.VALID
            isValid = true
        }

        return isValid

    }

    enum class ValidationState {
        INITIAL,
        VALID,
        INVALID_FIRST_NAME,
        INVALID_PROFILE_PIC,
        INVALID_LAST_NAME,
        INVALID_EMAIL,
        INVALID_MOBILE,
        INVALID_CATEGORY
    }
}