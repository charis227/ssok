package com.example.jiun.ssok.user.info

import android.support.v4.app.*
import android.support.v4.view.PagerAdapter

internal class UserInfoFragmentPagerAdapter(fm: FragmentManager, private val numberOfPages: Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        if (position < 0 || position >= numberOfPages)
            return null
        return when (position) {
            USER_INFO_1 -> UserInfo1Fragment()
            USER_INFO_2 -> UserInfo2Fragment()
            USER_INFO_3 -> UserInfo3Fragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return numberOfPages
    }

    override fun getItemPosition(item: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    companion object {
        const val USER_INFO_1 = 0
        const val USER_INFO_2 = 1
        const val USER_INFO_3 = 2
    }
}
