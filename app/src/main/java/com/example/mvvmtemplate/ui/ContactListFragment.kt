package com.example.mvvmtemplate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtemplate.MainActivity
import com.example.mvvmtemplate.R
import com.example.mvvmtemplate.database.AppDatabase
import com.example.mvvmtemplate.database.entity.Contact
import com.example.mvvmtemplate.databinding.FragmentContactListBinding
import com.example.mvvmtemplate.repository.ContactRepository
import com.example.mvvmtemplate.ui.adapter.ContactListAdapter
import com.example.mvvmtemplate.viewmodel.ContactViewModel
import com.example.mvvmtemplate.viewmodel.ContactsViewModelFactory

class ContactListFragment : Fragment() {

    private var binding: FragmentContactListBinding? = null
    private var contactListAdapter: ContactListAdapter? = null
    private var contactListRV: RecyclerView? = null


    private val onItemActionListener: ContactListAdapter.OnItemActionListener = object :
        ContactListAdapter.OnItemActionListener{
        override fun onItemDeleteClick(item: Contact?) {
            item?.let {
                contactViewModel.deleteContact(item)
            }
        }

        override fun onItemEditClick(item: Contact?) {
            item?.let {
                navigateForEdit(contact = it)
            }
        }
    }


    private val contactViewModel: ContactViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ContactsViewModelFactory(ContactRepository(AppDatabase.getDatabase(requireActivity())))
        )[ContactViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contact_list, container, false )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        initializeData()
    }

    private fun setUpViews(){
        binding?.let {binding ->
            contactListAdapter = ContactListAdapter(onItemActionListener)
            contactListRV = binding.rvContactList
            contactListRV?.apply {
                adapter = contactListAdapter
                layoutManager = LinearLayoutManager(this@ContactListFragment.context)
            }

        }
    }

    private fun initializeData() {
        contactViewModel.visibleContacts.observe(viewLifecycleOwner) { contacts ->
            contactListAdapter?.updateData(contacts)
        }
    }

    private fun navigateForEdit(contact:Contact) {
        if (activity is MainActivity) {
            val addContactFragment = AddContactFragment()
            addContactFragment.arguments = Bundle().apply {
                putParcelable("contact", contact)
            }

            (activity as MainActivity).navigateToFragment(addContactFragment)
        }
    }
}