package cn.com.caoyue.contacts0;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

public class InfoActivity extends AppCompatActivity {

    ContactItem contactItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //初始化朱大工具
        JUtils.initialize(getApplication());
        JUtils.setDebug(BuildConfig.DEBUG, "inInfo");
        JActivityManager.getInstance().pushActivity(this);
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
        long id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            JUtils.ToastLong(getResources().getString(R.string.unexpected_error));
            finish();
        }
        contactItem = ContactsControl.getContactsControl(InfoActivity.this).findContactItemById(id);
        //设置详情页显示
        ((TextView) findViewById(R.id.name)).setText(contactItem.getName());
        ((TextView) findViewById(R.id.phone_number)).setText(contactItem.getNumber());
        //[编辑]按钮
        ((CardView) findViewById(R.id.edit_button)).setOnClickListener(new ListenerInInfo());
        //[删除]按钮
        ((CardView) findViewById(R.id.delete_button)).setOnClickListener(new ListenerInInfo());
    }

    private class ListenerInInfo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_button:
                    EditActivity.actionStart(InfoActivity.this, contactItem.getId(), contactItem.getName(), contactItem.getNumber(), EditActivity.REQUEST_CODE_EDIT);
                    break;
                case R.id.delete_button:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JActivityManager.getInstance().popActivity(this);
    }

    //活动启动器
    public static void actionStart(Context context, long id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
