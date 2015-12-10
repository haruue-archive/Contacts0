package cn.com.caoyue.contacts0;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ContactItem> contactItemArrayList = new ArrayList<ContactItem>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //设置 ListView
        ListView contactList = (ListView) findViewById(R.id.list_contacts);
        contactList.setAdapter(new ContactAdapter(MainActivity.this, R.layout.contact_item, contactItemArrayList));
        contactList.setOnItemClickListener(new ListenerInMain());
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
            Log.e("Error_getContacts()", e.toString());
            Toast.makeText(getApplicationContext(), R.string.permission_error, Toast.LENGTH_LONG).show();
            finish();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private class ListenerInMain implements View.OnClickListener, AdapterView.OnItemClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InfoActivity.actionStart(MainActivity.this, contactItemArrayList.get(position).getId());
        }
    }
}
