package cn.com.caoyue.contacts0;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class ContactAdapter extends RecyclerArrayAdapter<ContactItem> {

    public ContactAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(parent);
    }


    private class ContactViewHolder extends BaseViewHolder<ContactItem> {

        private TextView nameView;
        private TextView numberView;

        public ContactViewHolder(ViewGroup parent) {
            super(parent, R.layout.contact_item);
            nameView = $(R.id.contact_item_name);
            numberView = $(R.id.contact_item_number);
        }

        @Override
        public void setData(final ContactItem contactItem) {
            nameView.setText(contactItem.getName());
            numberView.setText(contactItem.getNumber());
        }
    }
}
