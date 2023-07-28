package com.example.contactsbook.ui.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList


class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val tabFragmentsCreators: MutableList<Fragment> = ArrayList()

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]
    }

    fun addFragment(fragment: Fragment) {
        tabFragmentsCreators.add(fragment)
    }
}