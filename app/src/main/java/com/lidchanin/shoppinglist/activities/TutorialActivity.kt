package com.lidchanin.shoppinglist.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.lidchanin.shoppinglist.R
import com.lidchanin.shoppinglist.adapters.TutorialPagerAdapter
import com.lidchanin.shoppinglist.utils.TutorialManager
import kotlinx.android.synthetic.main.activity_main.*

class TutorialActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        setContentView(R.layout.activity_main)
        val tutorialImages = TutorialManager.getTutorialImageList(this)
        val tutorialStrings = TutorialManager.getTutorialTextList(this)
        tabLayout.setupWithViewPager(viewPager, true)
        previous.visibility = View.GONE
        previous.setOnClickListener {
            if (viewPager!!.currentItem - 1 >= 0) {
                viewPager!!.currentItem = viewPager!!.currentItem - 1
            }
            if (viewPager!!.currentItem == 0) {
                previous!!.visibility = View.GONE
            }
        }
        next!!.setOnClickListener {
            previous!!.visibility = View.VISIBLE
            if (viewPager!!.currentItem + 1 < tutorialImages.size) {
                viewPager!!.currentItem = viewPager!!.currentItem + 1
            } else {
                startActivity(Intent(this@TutorialActivity, ShoppingListFragmentManager::class.java))
            }
        }
        viewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (viewPager!!.currentItem == 0) {
                    previous!!.visibility = View.GONE
                } else {
                    previous!!.visibility = View.VISIBLE
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        viewPager!!.adapter = TutorialPagerAdapter(supportFragmentManager, tutorialImages, tutorialStrings)
    }

}