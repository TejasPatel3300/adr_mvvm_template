package com.example.silvertouchpracticaltest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.silvertouchpracticaltest.R
import com.example.silvertouchpracticaltest.database.AppDatabase
import com.example.silvertouchpracticaltest.database.entity.Category
import com.example.silvertouchpracticaltest.database.entity.Contact
import com.example.silvertouchpracticaltest.databinding.FragmentContactListBinding
import com.example.silvertouchpracticaltest.repository.ContactRepository
import com.example.silvertouchpracticaltest.ui.adapter.CategoriesAdapter
import com.example.silvertouchpracticaltest.ui.adapter.ContactListAdapter
import com.example.silvertouchpracticaltest.viewmodel.ContactViewModel
import com.example.silvertouchpracticaltest.viewmodel.ContactsViewModelFactory

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
            // todo: determine how we will create update category flow
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
        contactViewModel.contacts.observe(viewLifecycleOwner) {contacts ->
            contactListAdapter?.updateData(contacts)
        }
    }
}