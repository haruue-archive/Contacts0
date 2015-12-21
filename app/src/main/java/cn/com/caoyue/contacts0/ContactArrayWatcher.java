package cn.com.caoyue.contacts0;

import java.util.ArrayList;

public class ContactArrayWatcher {
    private ArrayList<ContactItem> addArray;
    private ArrayList<ContactItem> removeArray;
    private boolean isChanged;
    private static ContactArrayWatcher contactArrayWatcher;

    private ContactArrayWatcher() {
    }

    public static ContactArrayWatcher getContactArrayWatcher() {
        if (null == contactArrayWatcher) {
            contactArrayWatcher = new ContactArrayWatcher();
            contactArrayWatcher.init();
        }
        return contactArrayWatcher;
    }

    private void init() {
        isChanged = false;
        addArray = new ArrayList<ContactItem>();
        removeArray = new ArrayList<ContactItem>();
    }

    public ArrayList<ContactItem> getAddArray() {
        return addArray;
    }

    public ArrayList<ContactItem> getRemoveArray() {
        return removeArray;
    }

    public void add(ContactItem contactItem) {
        addArray.add(contactItem);
        isChanged = true;
    }

    public void remove(ContactItem contactItem) {
        removeArray.add(contactItem);
        isChanged = true;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void clear() {
        init();
    }
}
