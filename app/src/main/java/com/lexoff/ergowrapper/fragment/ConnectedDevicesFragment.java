package com.lexoff.ergowrapper.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.AllConnectedDevicesListAdapter;
import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.ConnectedDevicesListAdapter;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.info.ConnectedDevice;
import com.lexoff.ergowrapper.info.ConnectedDevicesInfo;
import com.lexoff.ergowrapper.info.ExtentedConnDevice;
import com.lexoff.ergowrapper.info.ExtentedConnDevicesInfo;
import com.lexoff.ergowrapper.info.ResponseInfo;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConnectedDevicesFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private ListView connectedDevicesListView;

    private boolean showAllDevices=false;

    private AtomicBoolean ignoreOneError=new AtomicBoolean(false);

    public ConnectedDevicesFragment() {
        //empty
    }

    public static ConnectedDevicesFragment newInstance() {
        ConnectedDevicesFragment fragment = new ConnectedDevicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.connected_devices_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.action_switch_devices_type){
            showAllDevices=!showAllDevices;

            loadInfo();

            new Handler(Looper.getMainLooper()).post(() ->
                    item.setTitle(showAllDevices ? "Show connected devices" : "Show all devices")
            );
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connected_devices, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Connected devices");

        refreshLayout=rootView.findViewById(R.id.cd_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadInfo());

        connectedDevicesListView = rootView.findViewById(R.id.cd_lv);
        connectedDevicesListView.setOnItemClickListener((parent, view, position, id) -> {
            if (showAllDevices) showExtentedDeviceInfoDialog(position);
            else showDeviceInfoDialog(position);
        });

        if (savedInstanceState == null)
            loadInfo();
    }

    private void loadInfo(){
        if (showAllDevices) loadAllConnectedDevices();
        else loadConnectedDevices();
    }

    private void loadAllConnectedDevices() {
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getAllConnectedDevicesInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ExtentedConnDevicesInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void loadConnectedDevices() {
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getConnectedDevicesInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ConnectedDevicesInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void blockDevice(String mac) {
        if (isLoading.get()) return;

        isLoading.set(true);
        ignoreOneError.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).blockDevice(mac))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    Toast.makeText(requireContext(), "Device will be blocked after WiFi restarts", Toast.LENGTH_SHORT).show();
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);

                    handleError(throwable);
                });
    }

    private void unblockDevice(String mac) {
        if (isLoading.get()) return;

        isLoading.set(true);
        ignoreOneError.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).unblockDevice(mac))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    Toast.makeText(requireContext(), "Device will be unblocked after WiFi restarts", Toast.LENGTH_SHORT).show();
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);

                    handleError(throwable);
                });
    }

    private void showExtentedDeviceInfoDialog(int pos){
        ExtentedConnDevice device=
                (ExtentedConnDevice) (connectedDevicesListView.getAdapter()).getItem(pos);

        StringBuilder message=new StringBuilder();
        message.append("Name: ");
        message.append(device.getName());
        message.append("\n");
        message.append("IP: ");
        message.append(device.getIP());
        message.append("\n");
        message.append("MAC: ");
        message.append(device.getMac());
        message.append("\n");
        message.append("\n");
        message.append("Connection type: ");
        message.append(device.getConnectionType());
        message.append("\n");
        message.append("Connection status: ");
        message.append(device.getStatus());
        message.append("\n");
        message.append("Last connection date: ");
        message.append(device.getLastConnectionTime());
        message.append("\n");
        message.append("Total connection time: ");
        message.append(device.getTotalConnectionTime());

        new AlertDialog.Builder(requireContext())
                .setTitle("Device's info")
                .setMessage(message.toString())
                .setCancelable(true)
                .setPositiveButton(device.isBlocked() ? "Unblock device" : "Block device", (dialog, which) -> {
                    if (device.isBlocked()) unblockDevice(device.getMac());
                    else blockDevice(device.getMac());
                })
                .create()
                .show();
    }

    private void showDeviceInfoDialog(int pos){
        ConnectedDevice device=
                (ConnectedDevice) (connectedDevicesListView.getAdapter()).getItem(pos);

        StringBuilder message=new StringBuilder();
        message.append("Name: ");
        message.append(device.getName());
        message.append("\n");
        message.append("IP: ");
        message.append(device.getIP());
        message.append("\n");
        message.append("MAC: ");
        message.append(device.getMac());
        message.append("\n");
        message.append("\n");
        message.append("Connection type: ");
        message.append(device.getConnectionType());
        message.append("\n");
        message.append("Connection time: ");
        message.append(device.getConnectionTime());

        new AlertDialog.Builder(requireContext())
                .setTitle("Device's info")
                .setMessage(message.toString())
                .setCancelable(true)
                .create()
                .show();
    }

    private void handleResult(ExtentedConnDevicesInfo info){
        AllConnectedDevicesListAdapter adapter = new AllConnectedDevicesListAdapter(requireContext(), info);
        connectedDevicesListView.setAdapter(adapter);
    }

    private void handleResult(ConnectedDevicesInfo info){
        ConnectedDevicesListAdapter adapter = new ConnectedDevicesListAdapter(requireContext(), info);
        connectedDevicesListView.setAdapter(adapter);
    }

    protected void handleError(Throwable e){
        if (ignoreOneError.getAndSet(false)) {
            Toast.makeText(requireContext(), "Device will be unblocked after WiFi restarts", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}
