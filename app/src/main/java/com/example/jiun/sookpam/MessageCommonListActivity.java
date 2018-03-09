package com.example.jiun.sookpam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jiun.sookpam.message.ContentActivity;
import com.example.jiun.sookpam.message.ContentItem;
import com.example.jiun.sookpam.model.ContactDBManager;
import com.example.jiun.sookpam.model.RecordDBManager;
import com.example.jiun.sookpam.model.vo.RecordVO;
import com.example.jiun.sookpam.user.setting.SettingCategory;
import com.example.jiun.sookpam.util.SharedPreferenceUtil;

import java.util.ArrayList;

import io.realm.Realm;

public class MessageCommonListActivity extends AppCompatActivity {
    private RecordDBManager categoryManager;
    private static final String DIVISION = "DVISION";
    private String division;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_common_list);

        MessageDepartListAdapter adapter = new MessageDepartListAdapter();
        ListView listView = findViewById(R.id.message_common_listview);
        listView.setAdapter(adapter);

        final ArrayList<RecordVO> datalist = new ArrayList<RecordVO>();
        ContactDBManager contactDBManager = (ContactDBManager) this.getApplicationContext();
        ArrayList<String> departmentList = contactDBManager.getDepartmentList();
        String category = "";

        for (int i=0; i<departmentList.size(); i++) {
            division = departmentList.get(i);
            category = contactDBManager.getCategory(division, Realm.getDefaultInstance());
            if (SharedPreferenceUtil.get(this, category, SettingCategory.NORMAL_CATEGORY) == SettingCategory.INTEREST_CATEGORY && category != "공통") {
                datalist.addAll(getDataByDivision(division));
                adapter.addItem(datalist);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordVO data = datalist.get(position);
                showMessageBody(data);
            }
        });

    }

    private ArrayList<RecordVO> getDataByDivision(String division) {
        categoryManager = new RecordDBManager(Realm.getDefaultInstance());
        ArrayList<RecordVO> response;
        if (!division.equals("공지"))
            response = categoryManager.getDataByDivision(division);
        else
            response = handleUnclipedCategories();

        return response;
    }

    private ArrayList<RecordVO> handleUnclipedCategories() {
        ContactDBManager contactDBManager = (ContactDBManager) getApplicationContext();
        ArrayList<String> divisionList = contactDBManager.getDepartmentList();
        ArrayList<RecordVO> response = new ArrayList<>();
        for (String division : divisionList) {
            if (!SharedPreferenceUtil.get(this, division, false))
                response.addAll(categoryManager.getDataByDivision(division));
        }
        return response;
    }

    private void showMessageBody(RecordVO data) {
        Intent intent = new Intent(this, ContentActivity.class);
        ContentItem contentItem  = new ContentItem();
        contentItem.setCategory(data.getCategory());
        contentItem.setDivision(data.getDivision());
        contentItem.setBody(data.getMessage().getBody());
        contentItem.setPhone(data.getMessage().getPhoneNumber());
        Bundle bundle = new Bundle();
        bundle.putSerializable("OBJECT", contentItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}