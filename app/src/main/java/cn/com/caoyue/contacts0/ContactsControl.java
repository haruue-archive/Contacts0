package cn.com.caoyue.contacts0;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jude.utils.JActivityManager;

import java.util.ArrayList;

/**
 * 通讯录统一管理，然而这并不是一个活动
 */
public class ContactsControl {
    private static ArrayList<ContactItem> contactItemArrayList;
    private static ContactsControl contactsControl;
    private static Context context;

    public static ContactsControl getContactsControl(Context mContext) {
        context = mContext;
        if (null == contactsControl) {
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
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactItemArrayList.add(new ContactItem(id, name, number));
                Log.e("arrayCon", id + name + number);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("ContactsControl", e.toString());
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
        long id;  //id将由系统决定
        String name = contactItem.getName();
        String number = contactItem.getNumber();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        id = ContentUris.parseId(resolver.insert(uri, values));
        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", id);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data2", name);
        resolver.insert(uri, values);
        values.clear();
        values.put("raw_contact_id", id);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");
        values.put("data1", number);
        resolver.insert(uri, values);
        //加入 contactItemArrayList
        contactItem.setId(id);
        contactItemArrayList.add(contactItem);
        return contactItem;
    }

    public ContactItem remove(ContactItem contactItem) {
        //从系统联系人数据表删除
        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=?", new String[]{contactItem.getId() + ""});
        //从 contactItemArrayList 删除
        contactItemArrayList.remove(contactItem);
        return contactItem;
    }

    public ContactItem update(int id, ContactItem newContactItem) {
        remove(findContactItemById(id));
        return add(newContactItem);
    }
}
