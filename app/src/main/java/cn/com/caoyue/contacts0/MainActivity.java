package cn.com.caoyue.contacts0;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ContactItem> contactItemArrayList = new ArrayList<ContactItem>(0);
    private EasyRecyclerView contactsRecyclerView;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化朱大工具
        JUtils.initialize(getApplication());
        JUtils.setDebug(BuildConfig.DEBUG, "inMain");
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inMain);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.button_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取通讯录
        getContacts();
        //初始化contacts view
        initContactsView();
    }

    /**
     * 初始化 EasyRecycler
     */
    private void initContactsView() {
        contactsRecyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView_contacts);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        contactsRecyclerView.setAdapterWithProgress(adapter = new ContactAdapter(MainActivity.this));
        adapter.addAll(contactItemArrayList);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            //动画
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            void rippleAnimation(int position) {
                final View view = findViewById((int) adapter.getItemId(position));
                view.animate().translationZ(15F).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.animate().translationZ(1f).setDuration(500).start();
                    }
                }).start();
            }

            @Override
            public void onItemClick(int position) {
                rippleAnimation(position);
                //跳转到详情页
                InfoActivity.actionStart(MainActivity.this, contactItemArrayList.get(position).getId());
            }
        });
    }

    /**
     * 获取系统通讯录信息并存储到 ArrayList 里
     */
    private void getContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactItemArrayList.add(new ContactItem(id, name, number));
            }
            cursor.close();
        } catch (Exception e) {
            JUtils.Log("Error_getContacts(): " + e.toString());
            JUtils.ToastLong(getResources().getString(R.string.permission_error));
            finish();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private class ListenerInMain implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }
}
