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
        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        TextView nameTextView = (TextView) view.findViewById(R.id.contact_item_name);
        TextView numberTextView = (TextView) view.findViewById(R.id.contact_item_number);
        nameTextView.setText(contactItem.getName());
        numberTextView.setText(contactItem.getNumber());
        return view;
    }
}
