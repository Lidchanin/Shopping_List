package com.lidchanin.shoppinglist.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lidchanin.shoppinglist.Constants
import com.lidchanin.shoppinglist.utils.SharedPrefsManager

class SplashScreen : AppCompatActivity(){

    private var sharedPrefsManager: SharedPrefsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefsManager = SharedPrefsManager(applicationContext)
        if (!sharedPrefsManager!!.readBoolean(Constants.Tutorial.IS_TUTORIAL)) {
            sharedPrefsManager!!.editBoolean(Constants.Tutorial.IS_TUTORIAL, true)
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, ShoppingListFragmentManager::class.java)
            startActivity(intent)
            finish()
        }
    }
}