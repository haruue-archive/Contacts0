package cn.com.caoyue.contacts0;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private int id;
    private String name;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inInfo);
        toolbar.setTitle(R.string.information_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.button_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //取回id
        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_LONG).show();
            finish();
        }
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id + ""}, null);
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
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
        //设置详情页显示
        ((TextView) findViewById(R.id.name)).setText(name);
        ((TextView) findViewById(R.id.phone_number)).setText(number);
    }

    //活动启动器
    public static void actionStart(Context context, int id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
