package com.lexoff.ergowrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lexoff.ergowrapper.info.ExtentedConnDevice;
import com.lexoff.ergowrapper.info.ExtentedConnDevicesInfo;

import java.util.ArrayList;

public class AllConnectedDevicesListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ExtentedConnDevice> items;

    public AllConnectedDevicesListAdapter(Context context, ExtentedConnDevicesInfo info) {
        this.context = context;
        this.items=info.getDevices();
    }

    public void addItems(ArrayList<ExtentedConnDevice> items){
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
        ExtentedConnDevice device=items.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.all_cd_list_item, parent, false);

        TextView name_tv = convertView.findViewById(R.id.name_tv);
        TextView mac_tv = convertView.findViewById(R.id.mac_tv);
        TextView time_tv = convertView.findViewById(R.id.time_tv);

        name_tv.setText(device.getName()+"\n["+device.getMac()+"]");
        mac_tv.setText(device.getStatus());
        time_tv.setText(device.getLastConnectionTime().replace(" ", "\n"));

        return convertView;
    }
}
