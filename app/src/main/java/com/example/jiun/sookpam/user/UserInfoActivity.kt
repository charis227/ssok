package com.example.jiun.sookpam.user

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.*
import com.example.jiun.sookpam.R
import com.example.jiun.sookpam.user.major.MajorList
import com.example.jiun.sookpam.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity() {
    private lateinit var viewPager: CustomViewPager
    private var currentFragment: Fragment? = null
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private var circleImageViewArrayList: ArrayList<ImageView> = ArrayList(3)
    private var pagerAdapter = SimpleFragmentPagerAdapter(supportFragmentManager, MAX_PAGE_SIZE)
    private var currentPage = SimpleFragmentPagerAdapter.USER_INFO_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        initialize()
        movePage()
    }

    private fun initialize() {
        initializeViewPager()
        initializeMoveButtons()
        initializeCircleImages()
    }

    private fun initializeViewPager() {
        viewPager = user_view_pager
        viewPager.apply {
            this.setPagingEnabled(false)
            this.adapter = pagerAdapter
            this.offscreenPageLimit = 3
            currentFragment = pagerAdapter.getItem(currentPage)
        }
    }

    private fun initializeMoveButtons() {
        previousButton = user_info_previous_btn
        nextButton = user_info_next_btn
    }

    private fun initializeCircleImages() {
        (0..3)
                .map {
                    resources.getIdentifier("user_info_circle" + it + "_img", "id"
                            , packageName)
                }
                .forEach { circleImageViewArrayList.add(findViewById(it)) }
    }

    private fun movePage() {
        movePrevious()
        moveNext()
    }

    private fun movePrevious() {
        previousButton.setOnClickListener {
            if (currentPage == SimpleFragmentPagerAdapter.USER_INFO_4) {
                nextButton.text = getText(R.string.user_info_next_page)
                pagerAdapter.notifyDataSetChanged()
            }
            if (currentPage > SimpleFragmentPagerAdapter.USER_INFO_1) {
                if (currentPage == SimpleFragmentPagerAdapter.USER_INFO_2) {
                    previousButton.visibility = View.INVISIBLE
                }
                currentPage -= 1
                viewPager.setCurrentItem(currentPage, true)
                changeCircleColor(MOVE_PREVIOUS_PAGE)
            }
        }
    }

    private fun moveNext() {
        nextButton.setOnClickListener {
            if (currentPage < SimpleFragmentPagerAdapter.USER_INFO_4) {
                if (isConditionsFulfilled()) {
                    if (currentPage == SimpleFragmentPagerAdapter.USER_INFO_3) {
                        nextButton.text = getText(R.string.user_info_done)
                        pagerAdapter.notifyDataSetChanged()
                    }
                    if (currentPage == SimpleFragmentPagerAdapter.USER_INFO_1) {
                        previousButton.visibility = View.VISIBLE
                    }
                    currentPage += 1
                    viewPager.setCurrentItem(currentPage, true)
                    changeCircleColor(MOVE_NEXT_PAGE)
                } else {
                    Toast.makeText(applicationContext, "선택되지 않은 항목이 존재합니다", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                finish()
            }
        }
    }

    private fun isConditionsFulfilled(): Boolean {
        when (currentPage) {
            SimpleFragmentPagerAdapter.USER_INFO_1 -> {
                var selectedMajorCount = 0
                MajorList.collegeAndMajors
                        .flatMap { it }
                        .filter { SharedPreferenceUtil.get(applicationContext, it, false) }
                        .forEach { selectedMajorCount += 1 }

                if (selectedMajorCount < 1) return false
            }
            SimpleFragmentPagerAdapter.USER_INFO_2 -> {
                val status = SharedPreferenceUtil.get(applicationContext, UserInfo2Fragment.STUDENT_STATUS, "")
                if (status == "") return false
            }
            SimpleFragmentPagerAdapter.USER_INFO_3 -> {
                if (countInterestCategories() < 3) return false
            }
        }
        return true
    }

    private fun countInterestCategories(): Int {
        return PersonalCategory.categories.count { getCategoryStatus(it) == PersonalCategory.INTEREST_CATEGORY }
    }

    private fun getCategoryStatus(key: String): Int {
        return SharedPreferenceUtil.get(applicationContext, key, PersonalCategory.NORMAL_CATEGORY)
    }

    private fun changeCircleColor(doesMovePrevious: Boolean) {
        val previousPage: Int =
                if (doesMovePrevious) {
                    currentPage + 1
                } else {
                    currentPage - 1
                }
        circleImageViewArrayList[previousPage].setImageResource(R.drawable.ic_default_circle)
        circleImageViewArrayList[currentPage].setImageResource(R.drawable.ic_pink_circle)
    }

    companion object {
        const val MAX_PAGE_SIZE = 4
        const val MOVE_PREVIOUS_PAGE = true
        const val MOVE_NEXT_PAGE = false
    }
}
