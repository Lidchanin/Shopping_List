package com.lidchanin.shoppinglist.activities

import android.os.Bundle
import android.view.KeyEvent
import com.lidchanin.shoppinglist.Constants
import com.lidchanin.shoppinglist.R
import com.lidchanin.shoppinglist.utils.ThemeManager

class ShoppingListFragmentManager : NavigationDrawerActivity() {

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID)
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_manager)
        if (intent.hasExtra(Constants.Bundles.VIEWPAGER_PAGE)) {
            initFragment(Constants.Bundles.SETTINGS_FRAGMENT_ID,
                    intent.getIntExtra(Constants.Bundles.VIEWPAGER_PAGE, 0))
        } else {
            initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID)
        }
    }
}