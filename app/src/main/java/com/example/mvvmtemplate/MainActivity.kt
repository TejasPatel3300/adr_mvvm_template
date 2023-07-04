package com.example.mvvmtemplate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtemplate.constants.DRAWER_MENU_ITEMS
import com.example.mvvmtemplate.databinding.ActivityMainBinding
import com.example.mvvmtemplate.ui.AddContactFragment
import com.example.mvvmtemplate.ui.CategoriesFragment
import com.example.mvvmtemplate.ui.ContactListFragment
import com.example.mvvmtemplate.ui.adapter.DrawerAdapter
import com.example.mvvmtemplate.utils.hideKeyboard

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var drawerLayout:DrawerLayout? = null
    private var drawerMenu: RecyclerView? = null
    private var drawerAdapter: DrawerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpViews()
        initialSetup()
    }

    private fun initialSetup() {
        val fragment = CategoriesFragment()
        binding?.fragmentContainer?.let { fragmentContainer ->
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainer.id, fragment, null)
                .commit()
        }
    }

    private fun setUpViews() {
        drawerAdapter = DrawerAdapter(
            DRAWER_MENU_ITEMS,
            listener = object : DrawerAdapter.OnItemClickListener {
                override fun onItemClick(drawerMenuItemIndex: Int?) {
                    val fragment: Fragment? = when (drawerMenuItemIndex) {
                        0 -> CategoriesFragment()
                        1 -> AddContactFragment()
                        2 -> ContactListFragment()
                        else -> null
                    }
                    navigateToFragment(fragment)
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
            it.topAppBar.setOnCreateContextMenuListener { menu, v, menuInfo ->
                Log.d("menu", "setOnCreateContextMenuListener")
            }

//            it.topAppBar.setOnMenuItemClickListener {menuItem ->
//                when (menuItem.itemId) {
//                    R.id.filter ->{
//                        true
//                    }
//                    R.id.search_bar -> {
//                        true
//                    }
//
//                    else -> false
//                }
//
//            }
            drawerMenu?.apply {
                adapter = drawerAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }


    }

    fun navigateToFragment(fragment: Fragment?) {
        binding?.fragmentContainer?.let { fragmentContainer ->
            fragment?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .replace(fragmentContainer.id, fragment, null)
                    .commit()
            }
        }
        drawerLayout?.close()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.search_bar -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}

        // viewmodel access with factory
        // create UI and bind the data with data binding
        // fragment navigation
        // drawer changes(probably recycler view withing the NavigationView to customize)
        // image picker logic
        // search/filter in contacts
