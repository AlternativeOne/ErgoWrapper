package com.lexoff.ergowrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lexoff.ergowrapper.info.Contact;
import com.lexoff.ergowrapper.info.ContactsInfo;

import java.util.ArrayList;

public class ContactsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> items;

    public ContactsListAdapter(Context context, ContactsInfo info) {
        this.context = context;
        this.items=info.getContacts();
    }

    public void addItems(ArrayList<Contact> items){
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = items.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.contacts_list_item, parent, false);

        TextView name_tv = convertView.findViewById(R.id.name_tv);
        TextView mobile_tv = convertView.findViewById(R.id.mobile_tv);

        name_tv.setText(contact.getDecodedName());
        mobile_tv.setText(contact.getMobile());

        return convertView;
    }
}
