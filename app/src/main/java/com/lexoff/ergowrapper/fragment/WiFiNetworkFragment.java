package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.info.WiFi;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WiFiNetworkFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private View wifiStatusRow;
    private View networkNameRow;
    private View safetyModeRow;
    private View channelRow;

    private TextView wifiStatusTV;
    private TextView networkNameTV;
    private TextView safetyModeTV;
    private TextView channelTV;

    public WiFiNetworkFragment() {
        //empty
    }

    public static WiFiNetworkFragment newInstance() {
        WiFiNetworkFragment fragment = new WiFiNetworkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_network, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("WiFi network");

        refreshLayout=rootView.findViewById(R.id.wn_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadInfo());

        wifiStatusRow=rootView.findViewById(R.id.wifi_status_row);
        networkNameRow=rootView.findViewById(R.id.network_name_row);
        safetyModeRow=rootView.findViewById(R.id.safety_mode_row);
        channelRow=rootView.findViewById(R.id.channel_row);

        wifiStatusTV=rootView.findViewById(R.id.wifi_status_tv);
        networkNameTV=rootView.findViewById(R.id.network_name_tv);
        safetyModeTV=rootView.findViewById(R.id.safety_mode_tv);
        channelTV=rootView.findViewById(R.id.channel_tv);

        if (savedInstanceState==null)
            loadInfo();
    }

    private void loadInfo() {
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getWiFiInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final WiFi result) -> {
                    isLoading.set(false);

                    refreshLayout.setRefreshing(false);

                    handleResult(result);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void handleResult(WiFi info){
        wifiStatusTV.setText(info.getStatus());
        networkNameTV.setText(info.getSSID());
        safetyModeTV.setText(info.getEncryption());
        channelTV.setText(info.getChannelAsString());

        wifiStatusRow.setVisibility(View.VISIBLE);
        networkNameRow.setVisibility(View.VISIBLE);
        safetyModeRow.setVisibility(View.VISIBLE);
        channelRow.setVisibility(View.VISIBLE);
    }

}
