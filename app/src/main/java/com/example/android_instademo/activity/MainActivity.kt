package com.example.android_instademo.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.android_instademo.R
import com.example.android_instademo.adapter.ViewPagerAdapter
import com.example.android_instademo.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Contains view pager with 5 fragments in MainActivity,
 * and pages can be controlled by BottomNavigationView
 */
class MainActivity : BaseActivity(), HomeFragment.HomeListener, UploadFragment.UploadListener {
    val TAG = MainActivity::class.java.simpleName
    var index = 0
    lateinit var homeFragment: HomeFragment
    lateinit var uploadFragment: UploadFragment
    lateinit var viewPager:ViewPager
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun scrollToUpload() {
        index = 2
        scrollByIndex(index)
    }

    override fun scrollToHome() {
        index = 0
        scrollByIndex(index)
    }

    private fun scrollByIndex(index:Int){
        viewPager.setCurrentItem(index)
        bottomNavigationView.getMenu().getItem(index).setChecked(true);
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewPager.setCurrentItem(0)
                }
                R.id.navigation_search -> {
                    viewPager.setCurrentItem(1)
                }
                R.id.navigation_upload -> {
                    viewPager.setCurrentItem(2)
                }
                R.id.navigation_favorite -> {
                    viewPager.setCurrentItem(3)
                }
                R.id.navigation_profile -> {
                    viewPager.setCurrentItem(4)
                }
            }
            true
        }

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                index = position
                bottomNavigationView.getMenu().getItem(index).setChecked(true);
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // Home and Upload Fragments are global for communication purpose
        homeFragment = HomeFragment()
        uploadFragment = UploadFragment()
        setupViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(homeFragment)
        adapter.addFragment(SearchFragment())
        adapter.addFragment(uploadFragment)
        adapter.addFragment(FavoriteFragment())
        adapter.addFragment(ProfileFragment())
        viewPager.adapter = adapter
    }

    fun setTransparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}