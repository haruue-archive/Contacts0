package cn.com.caoyue.contacts0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<ContactItem> {

    private int resourceID;

    public ContactAdapter(Context context, int resource, List<ContactItem> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactItem contactItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.nameView = (TextView) view.findViewById(R.id.contact_item_name);
            viewHolder.numberView = (TextView) view.findViewById(R.id.contact_item_number);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.nameView.setText(contactItem.getName());
        viewHolder.numberView.setText(contactItem.getNumber());
        return view;
    }

    public final class ViewHolder {
        public TextView nameView;
        public TextView numberView;
    }
}
