package com.lidchanin.shoppinglist.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.lidchanin.shoppinglist.R
import com.lidchanin.shoppinglist.adapters.TutorialPagerAdapter
import com.lidchanin.shoppinglist.customview.DesignedViewPager
import com.lidchanin.shoppinglist.utils.TutorialManager

class TutorialActivity : AppCompatActivity(){

    private var designedViewPager: DesignedViewPager? = null
    private var tabLayout: TabLayout? = null
    private var previous: Button? = null
    private var next: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        setContentView(R.layout.activity_main)
        designedViewPager = findViewById<View>(R.id.viewPager) as DesignedViewPager
        tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        val tutorialImages = TutorialManager.getTutorialImageList(this)
        val tutorialStrings = TutorialManager.getTutorialTextList(this)
        val tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout.setupWithViewPager(designedViewPager, true)
        previous = findViewById<View>(R.id.previous) as Button
        previous!!.visibility = View.GONE
        previous!!.setOnClickListener {
            if (designedViewPager!!.currentItem - 1 >= 0) {
                designedViewPager!!.currentItem = designedViewPager!!.currentItem - 1
            }
            if (designedViewPager!!.currentItem == 0) {
                previous!!.visibility = View.GONE
            }
        }
        next = findViewById<View>(R.id.next) as Button
        next!!.setOnClickListener {
            previous!!.visibility = View.VISIBLE
            if (designedViewPager!!.currentItem + 1 < tutorialImages.size) {
                designedViewPager!!.currentItem = designedViewPager!!.currentItem + 1
            } else {
                // TODO: 03.12.2017 make animation to hide load
                startActivity(Intent(this@TutorialActivity, ShoppingListFragmentManager::class.java))
            }
        }
        designedViewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (designedViewPager!!.currentItem == 0) {
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
        designedViewPager!!.adapter = TutorialPagerAdapter(supportFragmentManager, tutorialImages, tutorialStrings)
    }

}