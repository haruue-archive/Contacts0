package cn.com.caoyue.contacts0;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

import org.w3c.dom.Text;

public class EditActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW = 54646;
    public static final int REQUEST_CODE_EDIT = 35465;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //初始化朱大工具
        JUtils.initialize(getApplication());
        JUtils.setDebug(BuildConfig.DEBUG, "inEdit");
        JActivityManager.getInstance().pushActivity(this);
        //获取请求类型
        int requestCode = getIntent().getIntExtra("requestCode", -1);
        //杀掉异常请求
        if (requestCode != REQUEST_CODE_NEW && requestCode != REQUEST_CODE_EDIT) {
            unexpectedError("undefRequestCode: " + requestCode);
        }
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inEdit);
        toolbar.setTitle(requestCode == REQUEST_CODE_NEW ? R.string.new_contact : R.string.edit_contact);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.button_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //判断请求类型
        switch (requestCode) {
            case REQUEST_CODE_NEW:
                break;
            case REQUEST_CODE_EDIT:
                long id = getIntent().getLongExtra("id", -1);
                //杀掉异常请求
                if (id == -1) {
                    unexpectedError("GetIdFailed");
                }
                String origName = getIntent().getStringExtra("defaultName");
                String origNumber = getIntent().getStringExtra("defaultNumber");
                ((FloatLabel) findViewById(R.id.name_input)).setText(origName);
                ((FloatLabel) findViewById(R.id.number_input)).setText(origNumber);
                ((TextView) findViewById(R.id.ok_button)).setOnClickListener(new ListenerInEdit());
                ((TextView) findViewById(R.id.cancel_button)).setOnClickListener(new ListenerInEdit());
                break;
        }
    }

    //异常请求处理
    private void unexpectedError(String log) {
        JUtils.ToastLong(getResources().getString(R.string.unexpected_error));
        JUtils.Log(log);
        setResult(RESULT_CANCELED);
        finish();
    }

    private class ListenerInEdit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ok_button:
                    break;
                case R.id.cancel_button:
                    setResult(RESULT_CANCELED);
                    finish();
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
    public static void actionStart(Context context, long id, String defaultName, String defaultNumber, int requestCode) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("defaultName", defaultName);
        intent.putExtra("defaultNumber", defaultNumber);
        intent.putExtra("requestCode", requestCode);
        ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
    }
}
