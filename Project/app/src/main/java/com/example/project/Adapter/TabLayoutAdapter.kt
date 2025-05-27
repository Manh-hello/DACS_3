package com.example.project.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project.Fragment.TabLayout.ChoXacNhanFragment
import com.example.project.Fragment.TabLayout.DaHuyFragment
import com.example.project.Fragment.TabLayout.DaNhanHangFragment
import com.example.project.Fragment.TabLayout.DaXacNhanFragment
import com.example.project.Fragment.TabLayout.DangGiaoHangFragment

class TabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> ChoXacNhanFragment()
            1-> DaXacNhanFragment()
            2-> DangGiaoHangFragment()
            3 -> DaNhanHangFragment()
            else-> DaHuyFragment()
        }
    }

    override fun getItemCount(): Int = 5
}