package com.lexoff.ergowrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lexoff.ergowrapper.info.ConnectedDevice;
import com.lexoff.ergowrapper.info.ConnectedDevicesInfo;

import java.util.ArrayList;

public class ConnectedDevicesListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ConnectedDevice> items;

    public ConnectedDevicesListAdapter(Context context, ConnectedDevicesInfo info) {
        this.context = context;
        this.items=info.getDevices();
    }

    public void addItems(ArrayList<ConnectedDevice> items){
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
        ConnectedDevice device=items.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.cd_list_item, parent, false);

        TextView name_tv = convertView.findViewById(R.id.name_tv);
        //TextView mac_tv = convertView.findViewById(R.id.mac_tv);
        TextView time_tv = convertView.findViewById(R.id.time_tv);

        name_tv.setText(device.getName()+"\n["+device.getMac()+"]");
        //mac_tv.setText(device.getMac());
        time_tv.setText(device.getConnectionTime());

        return convertView;
    }
}
