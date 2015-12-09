package cn.com.caoyue.contacts0;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> names = new ArrayList<String>(0);
    private ArrayList<String> numbers = new ArrayList<String>(0);

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
        //设置 contactItemArrayList
        ArrayList<ContactItem> contactItemArrayList = new ArrayList<ContactItem>();
        for (int i = 0; i < names.size(); i++) {
            contactItemArrayList.add(new ContactItem(names.get(i), numbers.get(i)));
        }
        //设置 ListView
        ListView contactList = (ListView) findViewById(R.id.list_contacts);
        contactList.setAdapter(new ContactAdapter(MainActivity.this, R.layout.contact_item, contactItemArrayList));
    }

    /**
     * 获取系统通讯录信息并存储到 ArrayList 里
     */
    private void getContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                names.add(name);
                numbers.add(number);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Error_getContacts()", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }
}
