package com.example.jiun.sookpam.message

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.jiun.sookpam.R
import com.example.jiun.sookpam.clip.ClipDBManager
import com.example.jiun.sookpam.model.DualModel
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
        if (body.length < 20)
            title = body
        else
            title = body.substring(0, 20)
        title_view.text = title
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
            star.icon = resources.getDrawable(R.drawable.star_off)
        } else {
            star.icon = resources.getDrawable(R.drawable.star_on)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_star -> {
                if (dbmanager.doesNotExist(body)) {
                    item.icon = resources.getDrawable(R.drawable.star_on)
                    dbmanager.insert(body, DualModel.RECORD_VO)
                } else {
                    item.icon = resources.getDrawable(R.drawable.star_off)
                    dbmanager.delete(body)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
