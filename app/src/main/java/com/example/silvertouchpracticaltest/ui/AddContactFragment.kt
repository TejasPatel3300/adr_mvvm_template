package com.example.silvertouchpracticaltest.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.silvertouchpracticaltest.R
import com.example.silvertouchpracticaltest.database.AppDatabase
import com.example.silvertouchpracticaltest.database.entity.Category
import com.example.silvertouchpracticaltest.database.entity.Contact
import com.example.silvertouchpracticaltest.databinding.FragmentAddContactBinding
import com.example.silvertouchpracticaltest.repository.CategoryRepository
import com.example.silvertouchpracticaltest.repository.ContactRepository
import com.example.silvertouchpracticaltest.ui.adapter.CategoryDropdownAdapter
import com.example.silvertouchpracticaltest.viewmodel.CategoryViewModel
import com.example.silvertouchpracticaltest.viewmodel.CategoryViewModelFactory
import com.example.silvertouchpracticaltest.viewmodel.ContactViewModel
import com.example.silvertouchpracticaltest.viewmodel.ContactsViewModelFactory

class AddContactFragment : Fragment() {
    private var binding: FragmentAddContactBinding? = null

    private var ivProfile: ImageView? = null

    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etMobileNumber: EditText? = null
    private var etEmail: EditText? = null

    private var autoTvCategories: AutoCompleteTextView? = null
    private var categoriesDropdownAdapter: CategoryDropdownAdapter? = null

    private var categoriesList: MutableList<Category> = mutableListOf()

    private var btnSave: Button? = null

    private var selectedCategory: Category? = null
    private var profilePicUri: Uri? = null

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            CategoryViewModelFactory(CategoryRepository(AppDatabase.getDatabase(requireActivity())))
        )[CategoryViewModel::class.java]
    }


    private val contactViewModel: ContactViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ContactsViewModelFactory(ContactRepository(AppDatabase.getDatabase(requireActivity())))
        )[ContactViewModel::class.java]
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            profilePicUri = uri
            ivProfile?.let { Glide.with(this).load(uri).circleCrop().into(it) }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_contact, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        getCategories()
        observeValidation()
    }

    private fun getCategories() {
        categoryViewModel.categories.observe(viewLifecycleOwner) {
            categoriesList.clear()
            categoriesList.addAll(it)
            categoriesDropdownAdapter?.notifyDataSetChanged()
        }
    }

    private fun observeValidation() {
        contactViewModel.validationState.observe(viewLifecycleOwner) {validationState ->
            when (validationState) {
                ContactViewModel.ValidationState.INITIAL -> {}
                ContactViewModel.ValidationState.VALID -> {

                }ContactViewModel.ValidationState.INVALID_PROFILE_PIC -> Toast.makeText(requireContext(), resources.getString(R.string.invalid_profile_picture), Toast.LENGTH_LONG).show()
                ContactViewModel.ValidationState.INVALID_FIRST_NAME -> Toast.makeText(requireContext(), resources.getString(R.string.invalid_first_name), Toast.LENGTH_LONG).show()
                ContactViewModel.ValidationState.INVALID_LAST_NAME -> Toast.makeText(requireContext(), resources.getString(R.string.invalid_last_name), Toast.LENGTH_LONG).show()
                ContactViewModel.ValidationState.INVALID_EMAIL -> Toast.makeText(requireContext(), resources.getString(R.string.invalid_email), Toast.LENGTH_LONG).show()
                ContactViewModel.ValidationState.INVALID_MOBILE -> Toast.makeText(requireContext(), resources.getString(R.string.invalid_mobile_number), Toast.LENGTH_LONG).show()
                ContactViewModel.ValidationState.INVALID_CATEGORY -> Toast.makeText(requireContext(), resources.getString(R.string.invalid_category), Toast.LENGTH_LONG).show()

                else -> {}
            }
        }

    }

    private fun setUpViews() {
        binding?.let { binding ->
            ivProfile = binding.ivProfile
            ivProfile?.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            etFirstName = binding.etFirstName
            etLastName = binding.etLastName
            etMobileNumber = binding.etMobileNumber
            etEmail = binding.etEmail
            autoTvCategories = binding.autoCompleteCategory
            categoriesDropdownAdapter = CategoryDropdownAdapter(
                this@AddContactFragment.requireContext(),
                R.layout.category_dropdown_item,
                categoriesList
            )

            autoTvCategories?.setAdapter(categoriesDropdownAdapter)
            autoTvCategories?.setOnItemClickListener { _, _, position, _ ->
                selectedCategory = categoriesList[position]
                autoTvCategories?.setText(selectedCategory?.name)
            }
            btnSave = binding.btnSave
            btnSave?.setOnClickListener {
                onSaved()
            }
        }
    }

    private fun onSaved() {
        val contact = Contact(
            firstName = etFirstName?.text.toString() ?: "",
            lastName = etLastName?.text.toString() ?: "",
            email = etEmail?.text.toString() ?: "",
            mobileNumber = etMobileNumber?.text.toString() ?: "",
            profilePicUri = profilePicUri?.toString() ?: "",
            category = selectedCategory?.id ?: -1L
        )
        contactViewModel.insertContact(contact)
    }

}