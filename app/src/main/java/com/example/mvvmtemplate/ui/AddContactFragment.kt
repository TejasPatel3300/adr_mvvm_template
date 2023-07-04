package com.example.mvvmtemplate.ui

import android.net.Uri
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmtemplate.R
import com.example.mvvmtemplate.database.AppDatabase
import com.example.mvvmtemplate.database.entity.Category
import com.example.mvvmtemplate.database.entity.Contact
import com.example.mvvmtemplate.databinding.FragmentAddContactBinding
import com.example.mvvmtemplate.repository.CategoryRepository
import com.example.mvvmtemplate.repository.ContactRepository
import com.example.mvvmtemplate.ui.adapter.CategoryDropdownAdapter
import com.example.mvvmtemplate.utils.hideKeyboard
import com.example.mvvmtemplate.viewmodel.CategoryViewModel
import com.example.mvvmtemplate.viewmodel.CategoryViewModelFactory
import com.example.mvvmtemplate.viewmodel.ContactViewModel
import com.example.mvvmtemplate.viewmodel.ContactsViewModelFactory

class AddContactFragment : Fragment() {
    private var binding: FragmentAddContactBinding? = null
    private var bundleContact: Contact? = null

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
        bundleContact = when {
            VERSION.SDK_INT >= 33 -> arguments?.getParcelable("contact", Contact::class.java)
            else -> @Suppress("DEPRECATION") arguments?.getParcelable("contact") as Contact?
        }

        Log.d("TAG","onCreate in Add contact")
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

            bundleContact?.let {
                prefillDataForEditing()
            }
        }
    }

    private fun prefillDataForEditing() {
        /* Here we check if we got contact from bundle or not
        * And if we have a contact from bundle then we use its
        * values for prefilling the details
        * */
        bundleContact?.let { contact ->
            etFirstName?.setText(contact.firstName)
            etLastName?.setText(contact.lastName)
            etMobileNumber?.setText(contact.mobileNumber)
            etEmail?.setText(contact.email)
            val prefillCategory = categoriesList.find { category ->
                category.id == contact.category
            }
            prefillCategory?.let {
                selectedCategory = it
                autoTvCategories?.setText(it.name)
            }
            profilePicUri = Uri.parse(contact.profilePicUri)
            ivProfile?.let { Glide.with(this).load(contact.profilePicUri).circleCrop().into(it) }
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
        if (bundleContact != null) {
            val contact = bundleContact!!.copy(
                firstName = etFirstName?.text.toString(),
                lastName = etLastName?.text.toString(),
                email = etEmail?.text.toString(),
                mobileNumber = etMobileNumber?.text.toString(),
                profilePicUri = profilePicUri?.toString() ?: "",
                category = selectedCategory?.id ?: -1L
            )
            contactViewModel.updateContact(contact)
        } else {
            val contact = Contact(
                firstName = etFirstName?.text.toString(),
                lastName = etLastName?.text.toString(),
                email = etEmail?.text.toString(),
                mobileNumber = etMobileNumber?.text.toString(),
                profilePicUri = profilePicUri?.toString() ?: "",
                category = selectedCategory?.id ?: -1L
            )
            contactViewModel.insertContact(contact)
        }
        hideKeyboard(requireActivity())
    }
}