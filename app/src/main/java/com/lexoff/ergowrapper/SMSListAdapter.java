package com.lexoff.ergowrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lexoff.ergowrapper.info.SMS;
import com.lexoff.ergowrapper.info.SMSInfo;

import java.util.ArrayList;

public class SMSListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SMS> items;

    public SMSListAdapter(Context context, SMSInfo info) {
        this.context = context;
        this.items=info.getSMS();
    }

    public void addItems(ArrayList<SMS> items){
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    public void setSMSRead(int position) {
        SMS sms = items.get(position);

        if (!sms.isRead()) {
            sms.setRead(true);

            items.remove(position);
            items.add(position, sms);

            notifyDataSetChanged();
        }
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
        SMS sms = items.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.sms_list_item, parent, false);

        TextView addresser_tv = convertView.findViewById(R.id.addresser_tv);
        TextView body_tv = convertView.findViewById(R.id.body_tv);
        TextView date_tv = convertView.findViewById(R.id.date_tv);
        CheckBox read_cb = convertView.findViewById(R.id.read_cb);

        addresser_tv.setText(sms.getAddresser());
        body_tv.setText((sms.getTotalSegmentsNum() > 0 ? sms.getSegmentsString()+" " : "") + sms.getDecodedBody() + "...");
        date_tv.setText(sms.getPrettiedArrivalTime());
        read_cb.setChecked(!sms.isRead());

        return convertView;
    }

}
