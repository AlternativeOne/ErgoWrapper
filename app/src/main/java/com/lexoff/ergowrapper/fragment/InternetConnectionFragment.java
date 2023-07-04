package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.info.CellNetworkInfo;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InternetConnectionFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private View networkOperatorRow;
    private View networkModeRow;
    private View connectionModeRow;
    private View apnNameRow;
    private View connectionStatusRow;

    private View ipv4AddressRow;
    private View ipv4Dns1Row;
    private View ipv4Dns2Row;
    private View ipv4DefaultGatewayRow;
    private View ipv4NetworkMaskRow;

    private View ipv6AddressRow;
    private View ipv6Dns1Row;
    private View ipv6Dns2Row;
    private View ipv6DefaultGatewayRow;
    private View ipv6NetworkMaskRow;

    private TextView networkOperatorTV;
    private TextView networkModeTV;
    private TextView connectionModeTV;
    private TextView apnNameTV;
    private TextView connectionStatusTV;

    private TextView ipv4AddressTV;
    private TextView ipv4Dns1TV;
    private TextView ipv4Dns2TV;
    private TextView ipv4DefaultGatewayTV;
    private TextView ipv4NetworkMaskTV;

    private TextView ipv6AddressTV;
    private TextView ipv6Dns1TV;
    private TextView ipv6Dns2TV;
    private TextView ipv6DefaultGatewayTV;
    private TextView ipv6NetworkMaskTV;

    public InternetConnectionFragment() {
        //empty
    }

    public static InternetConnectionFragment newInstance() {
        InternetConnectionFragment fragment = new InternetConnectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_internet_connection, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Internet connection");

        refreshLayout=rootView.findViewById(R.id.ic_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadInfo());

        networkOperatorRow=rootView.findViewById(R.id.network_operator_row);
        networkModeRow=rootView.findViewById(R.id.network_mode_row);
        connectionModeRow=rootView.findViewById(R.id.connection_mode_row);
        apnNameRow=rootView.findViewById(R.id.apn_name_row);
        connectionStatusRow=rootView.findViewById(R.id.connection_status_row);

        ipv4AddressRow=rootView.findViewById(R.id.ipv4_address_row);
        ipv4Dns1Row=rootView.findViewById(R.id.ipv4_dns1_row);
        ipv4Dns2Row=rootView.findViewById(R.id.ipv4_dns2_row);
        ipv4DefaultGatewayRow=rootView.findViewById(R.id.ipv4_default_gateway_row);
        ipv4NetworkMaskRow=rootView.findViewById(R.id.ipv4_network_mask_row);

        ipv6AddressRow=rootView.findViewById(R.id.ipv6_address_row);
        ipv6Dns1Row=rootView.findViewById(R.id.ipv6_dns1_row);
        ipv6Dns2Row=rootView.findViewById(R.id.ipv6_dns2_row);
        ipv6DefaultGatewayRow=rootView.findViewById(R.id.ipv6_default_gateway_row);
        ipv6NetworkMaskRow=rootView.findViewById(R.id.ipv6_network_mask_row);

        networkOperatorTV=rootView.findViewById(R.id.network_operator_tv);
        networkModeTV=rootView.findViewById(R.id.network_mode_tv);
        connectionModeTV=rootView.findViewById(R.id.connection_mode_tv);
        apnNameTV=rootView.findViewById(R.id.apn_name_tv);
        connectionStatusTV=rootView.findViewById(R.id.connection_status_tv);

        ipv4AddressTV=rootView.findViewById(R.id.ipv4_address_tv);
        ipv4Dns1TV=rootView.findViewById(R.id.ipv4_dns1_tv);
        ipv4Dns2TV=rootView.findViewById(R.id.ipv4_dns2_tv);
        ipv4DefaultGatewayTV=rootView.findViewById(R.id.ipv4_default_gateway_tv);
        ipv4NetworkMaskTV=rootView.findViewById(R.id.ipv4_network_mask_tv);

        ipv6AddressTV=rootView.findViewById(R.id.ipv6_address_tv);
        ipv6Dns1TV=rootView.findViewById(R.id.ipv6_dns1_tv);
        ipv6Dns2TV=rootView.findViewById(R.id.ipv6_dns2_tv);
        ipv6DefaultGatewayTV=rootView.findViewById(R.id.ipv6_default_gateway_tv);
        ipv6NetworkMaskTV=rootView.findViewById(R.id.ipv6_network_mask_tv);

        if (savedInstanceState==null)
            loadInfo();
    }

    private void loadInfo(){
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getCellNetworkInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final CellNetworkInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void handleResult(CellNetworkInfo info){
        networkOperatorTV.setText(info.getNetworkName());
        networkModeTV.setText(info.getNetworkMode());
        connectionModeTV.setText(info.getDataConnectionMode());
        apnNameTV.setText(info.getApnName());
        connectionStatusTV.setText(info.getConnectionStatus());

        networkOperatorRow.setVisibility(View.VISIBLE);
        networkModeRow.setVisibility(View.VISIBLE);
        connectionModeRow.setVisibility(View.VISIBLE);
        apnNameRow.setVisibility(View.VISIBLE);
        connectionStatusRow.setVisibility(View.VISIBLE);

        int ipType=info.getIpType();

        if (ipType==0 || ipType==1){
            ipv4AddressTV.setText(info.getIpv4Addr());
            ipv4Dns1TV.setText(info.getIpv4Dns1());
            ipv4Dns2TV.setText(info.getIpv4Dns2());
            ipv4DefaultGatewayTV.setText(info.getIpv4GateWay());
            ipv4NetworkMaskTV.setText(info.getIpv4NetMask());

            ipv4AddressRow.setVisibility(View.VISIBLE);
            ipv4Dns1Row.setVisibility(View.VISIBLE);
            ipv4Dns2Row.setVisibility(View.VISIBLE);
            ipv4DefaultGatewayRow.setVisibility(View.VISIBLE);
            ipv4NetworkMaskRow.setVisibility(View.VISIBLE);
        }

        if (ipType==0 || ipType==2){
            ipv6AddressTV.setText(info.getIpv6Addr());
            ipv6Dns1TV.setText(info.getIpv6Dns1());
            ipv6Dns2TV.setText(info.getIpv6Dns2());
            ipv6DefaultGatewayTV.setText(info.getIpv6GateWay());
            ipv6NetworkMaskTV.setText(info.getIpv6NetMask());

            ipv6AddressRow.setVisibility(View.VISIBLE);
            ipv6Dns1Row.setVisibility(View.VISIBLE);
            ipv6Dns2Row.setVisibility(View.VISIBLE);
            ipv6DefaultGatewayRow.setVisibility(View.VISIBLE);
            ipv6NetworkMaskRow.setVisibility(View.VISIBLE);
        }
    }

}
