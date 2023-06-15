package com.example.silvertouchpracticaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.silvertouchpracticaltest.constants.DRAWER_MENU_ITEMS
import com.example.silvertouchpracticaltest.database.AppDatabase
import com.example.silvertouchpracticaltest.databinding.ActivityMainBinding
import com.example.silvertouchpracticaltest.repository.CategoryRepository
import com.example.silvertouchpracticaltest.ui.AddContactFragment
import com.example.silvertouchpracticaltest.ui.CategoriesFragment
import com.example.silvertouchpracticaltest.ui.ContactListFragment
import com.example.silvertouchpracticaltest.ui.adapter.DrawerAdapter
import com.example.silvertouchpracticaltest.utils.hideKeyboard

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var drawerLayout:DrawerLayout? = null
    private var drawerMenu: RecyclerView? = null
    private var drawerAdapter: DrawerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpViews()
    }

    private fun setUpViews() {
        drawerAdapter = DrawerAdapter(
            DRAWER_MENU_ITEMS,
            listener = object : DrawerAdapter.OnItemClickListener {
                override fun onItemClick(drawerMenuItemIndex: Int?) {
                    navigateToFragment(drawerMenuItemIndex)
                }
            })
        binding?.let {
            drawerLayout = it.mainDrawer
            drawerMenu = it.rvDrawerMenu
            setSupportActionBar(it.topAppBar)
            it.topAppBar.setNavigationOnClickListener {
                hideKeyboard(this)
                drawerLayout?.open()
            }
            drawerMenu?.apply {
                adapter = drawerAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }


    }

    private fun navigateToFragment(drawerMenuItemIndex: Int?) {
        val fragment: Fragment? = when (drawerMenuItemIndex) {
            0 -> CategoriesFragment()
            1 -> AddContactFragment()
            2 -> ContactListFragment()
            else -> null
        }
        binding?.fragmentContainer?.let { fragmentContainer ->
            fragment?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .replace(fragmentContainer.id, fragment, null)
                    .commit()
            }
        }
        drawerLayout?.close()
    }

    private fun a(){
        val db = AppDatabase.getDatabase(this)
        val categoryRepository = CategoryRepository(db)
        // viewmodel access with factory
        // create UI and bind the data with data binding
        // fragment navigation
        // drawer changes(probably recycler view withing the NavigationView to customize)
        // image picker logic
        // search/filter in contacts
    }
}