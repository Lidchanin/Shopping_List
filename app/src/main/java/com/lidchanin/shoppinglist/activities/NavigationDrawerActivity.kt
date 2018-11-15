package com.lidchanin.shoppinglist.activities

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import com.lidchanin.shoppinglist.Constants
import com.lidchanin.shoppinglist.R
import com.lidchanin.shoppinglist.adapters.ProfitAdapter
import com.lidchanin.shoppinglist.fragments.*
import com.lidchanin.shoppinglist.utils.ThemeManager
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.activity_drawer.*

open class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var transformation: Transformation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager(this)
        super.onCreate(savedInstanceState)

    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(R.layout.activity_drawer)
        layoutInflater.inflate(layoutResID, container)
        initNavigationDrawer()
        initializeViewsAndButtons()
    }

    private fun initNavigationDrawer() {
        transformation = RoundedTransformationBuilder().oval(true).build()
        val toggle = ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer!!.setDrawerListener(toggle)
        navigationView.setNavigationItemSelectedListener(this)
        toggle.syncState()
    }

    fun setButtonsToDefault() {
        addItem!!.visibility = View.GONE
        alphabetSort!!.visibility = View.GONE
        dateSort!!.visibility = View.GONE
    }

    private fun initializeViewsAndButtons() {
        hamburger!!.setOnClickListener { drawer!!.openDrawer(Gravity.START) }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.nav_lists -> initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID)
            R.id.nav_existing_products -> initFragment(Constants.Bundles.ALL_PRODUCTS_FRAGMENT_ID)
            R.id.nav_profit -> initFragment(Constants.Bundles.PROFIT_FRAGMENT_ID)
            R.id.nav_statistics -> initFragment(Constants.Bundles.STATISTICS_FRAGMENT_ID)
            R.id.nav_settings -> initFragment(Constants.Bundles.SETTINGS_FRAGMENT_ID)
            R.id.nav_about_us -> initFragment(Constants.Bundles.ABOUT_US_FRAGMENT_ID)
        }
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    fun addNewItem(profitAdapter: ProfitAdapter) {
        addItem!!.visibility = View.VISIBLE
        addItem!!.setOnClickListener { profitAdapter.addNewItem() }
    }

    fun addNewItem(): ImageButton? {
        return addItem
    }

    fun setTitle(title: String) {
        pageTitle!!.text = title
    }


    fun initFragment(fragmentExtra: String) {
        initFragment(fragmentExtra, 0)
    }

    fun initFragment(fragmentExtra: String, page: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        setButtonsToDefault()
        when (fragmentExtra) {
            Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID -> {
                setTitle(getString(R.string.lists))
                val shoppingListFragment = ShoppingListFragment()
                fragmentTransaction.replace(R.id.container, shoppingListFragment)
                fragmentTransaction.commit()
            }
            Constants.Bundles.STATISTICS_FRAGMENT_ID -> {
                setTitle(getString(R.string.statistics))
                val statisticsFragment = StatisticsFragment()
                fragmentTransaction.replace(R.id.container, statisticsFragment)
                fragmentTransaction.commit()
            }
            Constants.Bundles.PROFIT_FRAGMENT_ID -> {
                setTitle(getString(R.string.profit))
                val profitFragment = ProfitFragment()
                fragmentTransaction.replace(R.id.container, profitFragment)
                fragmentTransaction.commit()
            }
            Constants.Bundles.SETTINGS_FRAGMENT_ID -> {
                setTitle(getString(R.string.themes))
                val settingsFragment = SettingsFragment()
                if (page != 0) {
                    val bundle = Bundle()
                    bundle.putInt(Constants.Bundles.VIEWPAGER_PAGE, page)
                    settingsFragment.arguments = bundle
                }
                fragmentTransaction.replace(R.id.container, settingsFragment)
                fragmentTransaction.commit()
            }
            Constants.Bundles.ALL_PRODUCTS_FRAGMENT_ID -> {
                setTitle(getString(R.string.all_products))
                val allProductsFragment = AllProductsFragment()
                fragmentTransaction.replace(R.id.container, allProductsFragment)
                fragmentTransaction.commit()
            }
            Constants.Bundles.ABOUT_US_FRAGMENT_ID -> {
                setTitle(getString(R.string.about_us))
                val aboutUsFragment = AboutUsFragment()
                fragmentTransaction.replace(R.id.container, aboutUsFragment)
                fragmentTransaction.commit()
            }
        }
    }

}