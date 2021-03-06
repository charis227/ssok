package com.example.jiun.ssok.message

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.jiun.ssok.user.mypage.MyPageBaseFragment
import com.example.jiun.ssok.R
import com.example.jiun.ssok.user.mypage.clip.ClipDBManager
import com.example.jiun.ssok.model.DualModel
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_content.*
import java.text.SimpleDateFormat


class ContentActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var dbmanager: ClipDBManager
    private lateinit var title: String
    private lateinit var body: String
    private lateinit var date: String
    private var category: String? = ""
    private var division: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        setContentData()
        setToolbar("메세지 > $category > $division")
    }

    private fun setContentData() {
        val intent = intent
        val record: ContentItem = intent.getSerializableExtra("OBJECT") as ContentItem
        division = record.division
        category = record.category
        body = record.body
        if (body!!.contains("[Web발신]"))
            body = body.replace("[Web발신]", "")
        body = body.replaceFirst("\n".toRegex(), "")
        title_view.text = body
        content_view.text = body
        info_view.text = "$division / ${record.phone}"
        val pattern = "yyyy-MM-dd"
        var simpleDateFormat = SimpleDateFormat(pattern)
        date =simpleDateFormat.format( record.date)
        date_view.text = date
    }

    private fun setToolbar(path: String) {
        toolbar = content_toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.title = path
        toolbar.setTitleTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener({
            finish()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        var star = menu!!.findItem(R.id.action_star)
        dbmanager = ClipDBManager(Realm.getDefaultInstance());
        if (dbmanager.doesNotExist(body)) {
            star.icon = ContextCompat.getDrawable(applicationContext, R.drawable.star_off)
        } else {
            star.icon = ContextCompat.getDrawable(applicationContext, R.drawable.star_on)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_star -> {
                if (dbmanager.doesNotExist(body)) {
                    item.icon = ContextCompat.getDrawable(applicationContext, R.drawable.star_on)
                    dbmanager.insert(body, DualModel.RECORD_VO, date)
                } else {
                    item.icon = ContextCompat.getDrawable(applicationContext, R.drawable.star_off)
                    dbmanager.delete(body)
                }
                MyPageBaseFragment.myPageViewPagerAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
