package cn.com.caoyue.contacts0;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

public class InfoActivity extends AppCompatActivity {

    ContactItem contactItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_info);
        } else {
            setContentView(R.layout.activity_info_v20);
        }
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
        update();
        //[编辑]按钮
        ((CardView) findViewById(R.id.edit_button)).setOnClickListener(new ListenerInInfo());
        //[删除]按钮
        ((CardView) findViewById(R.id.delete_button)).setOnClickListener(new ListenerInInfo());
        //[呼叫]按钮
        ((CardView) findViewById(R.id.call_button)).setOnClickListener(new ListenerInInfo());
        //[复制]按钮
        ((CardView) findViewById(R.id.copy_button)).setOnClickListener(new ListenerInInfo());
        //[短信]按钮
        ((CardView) findViewById(R.id.msg_button)).setOnClickListener(new ListenerInInfo());
    }

    private class ListenerInInfo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_button:
                    EditActivity.actionStart(InfoActivity.this, contactItem.getId(), contactItem.getName(), contactItem.getNumber(), EditActivity.REQUEST_CODE_EDIT);
                    break;
                case R.id.delete_button:
                    //显示对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                    builder.setTitle(R.string.delete_contact);
                    builder.setMessage(R.string.tip_delete_contact);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContactsControl.getContactsControl(InfoActivity.this).remove(contactItem);
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    break;
                case R.id.call_button:
                    Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactItem.getNumber()));
                    startActivity(intentDial);
                    JActivityManager.getInstance().closeAllActivity();
                    System.exit(0);
                    break;
                case R.id.copy_button:
                    JUtils.copyToClipboard(contactItem.getName() + " " + contactItem.getNumber());
                    JUtils.Toast(getResources().getString(R.string.copy_to_clipboard_success));
                    break;
                case R.id.msg_button:
                    Intent intentSms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contactItem.getNumber()));
                    startActivity(intentSms);
                    JActivityManager.getInstance().closeAllActivity();
                    System.exit(0);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JActivityManager.getInstance().popActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        update();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra("id", -1);
            if (id == -1) {
                unexpectedError("unexpectedID");
            }
            String name = data.getStringExtra("name");
            String number = data.getStringExtra("number");
            contactItem = new ContactItem(id, name, number);
            update();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //刷新界面
    private void update() {
        //设置详情页显示
        ((TextView) findViewById(R.id.name)).setText(contactItem.getName());
        ((TextView) findViewById(R.id.phone_number)).setText(contactItem.getNumber());
    }

    //异常请求处理
    private void unexpectedError(String log) {
        JUtils.ToastLong(getResources().getString(R.string.unexpected_error));
        JUtils.Log(log);
        finish();
    }

    //活动启动器
    public static void actionStart(Context context, long id) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
