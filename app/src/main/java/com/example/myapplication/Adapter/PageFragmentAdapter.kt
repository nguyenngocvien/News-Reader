package com.example.myapplication.Adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.myapplication.Fragment.PageFragment


class PageFragmentAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf("Tổng hợp", "Thời sự" ,"Thể thao", "Giải trí", "Kinh tế","Công nghệ", "Pháp luật","Cộng đồng","Giáo dục")

    override fun getCount(): Int {
        return tabTitles.count()
    }

    override fun getItem(position: Int): Fragment {
        return PageFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

}