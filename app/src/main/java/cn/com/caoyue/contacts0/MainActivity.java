package cn.com.caoyue.contacts0;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

public class MainActivity extends AppCompatActivity {

    private EasyRecyclerView contactsRecyclerView;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化朱大工具
        JUtils.initialize(getApplication());
        JUtils.setDebug(BuildConfig.DEBUG, "inMain");
        JActivityManager.getInstance().pushActivity(this);
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
        ContactsControl.getContactsControl(MainActivity.this);
        //初始化contacts view
        initContactsView();
        //[添加]按钮
        FloatingActionButton addButton = ((FloatingActionButton) findViewById(R.id.add_button));
        addButton.setOnClickListener(new ListenerInMain());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JActivityManager.getInstance().popActivity(this);
    }

    /**
     * 初始化 EasyRecycler
     */
    private void initContactsView() {
        contactsRecyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView_contacts);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        contactsRecyclerView.setAdapterWithProgress(adapter = new ContactAdapter(MainActivity.this));
        adapter.addAll(ContactsControl.getContactsControl(MainActivity.this).getContactItemArrayList());
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            //动画
            void rippleAnimation(int position) {
                final View view = findViewById((int) adapter.getItemId(position));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.animate().translationZ(15F).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                view.animate().translationZ(1f).setDuration(500).start();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onItemClick(int position) {
                rippleAnimation(position);
                //跳转到详情页
                InfoActivity.actionStart(MainActivity.this, adapter.getItem(position).getId());
            }
        });
        update();
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

    /**
     * 刷新表
     */
    private void update() {
        if (ContactArrayWatcher.getContactArrayWatcher().isChanged()) {
            adapter.addAll(ContactArrayWatcher.getContactArrayWatcher().getAddArray());
            for (ContactItem i : ContactArrayWatcher.getContactArrayWatcher().getRemoveArray()) {
                try {
                    int position = adapter.getPosition(i);
                    adapter.remove(position);
                    adapter.notifyItemRemoved(position);
                } catch (Exception ignored) {

                }
            }
            ContactArrayWatcher.getContactArrayWatcher().clear();
        }
    }

    private class ListenerInMain implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_button:
                    EditActivity.actionStart(MainActivity.this, 0, "", "", EditActivity.REQUEST_CODE_NEW);
                    break;
            }
        }
    }
}
