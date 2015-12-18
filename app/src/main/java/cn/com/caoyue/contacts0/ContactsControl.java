package cn.com.caoyue.contacts0;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.jude.utils.JActivityManager;

import java.util.ArrayList;

/**
 * 通讯录统一管理
 */
public class ContactsControl {
    private static ArrayList<ContactItem> contactItemArrayList;
    private static ContactsControl contactsControl;
    private static Context context;
    private static boolean isGotContacts = false;

    public static ContactsControl getContactsControl(Context mContext) {
        context = mContext;
        if (null == contactsControl || !isGotContacts) {
            contactsControl = new ContactsControl();
            contactsControl.init();
        }
        return contactsControl;
    }

    private void init() {
        //获取通讯录信息并存储于 ArrayList 中
        contactItemArrayList = new ArrayList<ContactItem>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactItemArrayList.add(new ContactItem(id, name, number));
            }
            cursor.close();
            isGotContacts = true;
        } catch (Exception e) {
            Toast.makeText(context, R.string.permission_error, Toast.LENGTH_LONG).show();
            Log.e("ContactsControl", e.toString());
            isGotContacts = false;
            JActivityManager.getInstance().closeAllActivity();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<ContactItem> getContactItemArrayList() {
        return contactItemArrayList;
    }

    public ContactItem findContactItemById(long id) {
        for (ContactItem i : contactItemArrayList) {
            if (i.getId() == id) return i;
        }
        return null;
    }

    public ContactItem add(ContactItem contactItem) {
        //加入系统联系人数据表
        long _id;  //id将由系统决定
        String name = contactItem.getName();
        String number = contactItem.getNumber();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        _id = ContentUris.parseId(resolver.insert(uri, values));
        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", _id);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data2", name);
        resolver.insert(uri, values);
        values.clear();
        values.put("raw_contact_id", _id);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");
        values.put("data1", number);
        resolver.insert(uri, values);
        //获取id
        Cursor cursor = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + "=?", new String[]{_id + ""}, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        cursor.close();
        //加入 contactItemArrayList
        contactItem.setId(id);
        contactItemArrayList.add(contactItem);
        //添加到联系人变化监视器
        ContactArrayWatcher.getContactArrayWatcher().add(contactItem);
        return contactItem;
    }

    public ContactItem remove(ContactItem contactItem) {
        //从系统联系人数据表删除
        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{contactItem.getId() + ""});
        //从 contactItemArrayList 删除
        contactItemArrayList.remove(contactItem);
        //添加到联系人变化监视器
        ContactArrayWatcher.getContactArrayWatcher().remove(contactItem);
        return contactItem;
    }

    public ContactItem update(long id, ContactItem newContactItem) {
        remove(findContactItemById(id));
        return add(newContactItem);
    }
}
