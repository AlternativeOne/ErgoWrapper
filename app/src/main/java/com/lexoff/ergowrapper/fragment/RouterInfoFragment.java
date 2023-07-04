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
import com.lexoff.ergowrapper.info.CellNetworkInfo;
import com.lexoff.ergowrapper.info.LANIPInfo;
import com.lexoff.ergowrapper.info.RuntimeInfo;
import com.lexoff.ergowrapper.info.WiFi;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RouterInfoFragment extends BaseFragment {
    private SwipeRefreshLayout refreshLayout;

    private View lanipRow;
    private View networkMaskRow;
    private View imeiRow;
    private View macRow;
    private View runtimeRow;

    private TextView lanipTV;
    private TextView networkMaskTV;
    private TextView imeiTV;
    private TextView macTV;
    private TextView runtimeTV;

    private CompositeDisposable compositeDisposable;
    private AtomicInteger completionCounter=new AtomicInteger(0);

    public RouterInfoFragment() {
        //empty
    }

    public static RouterInfoFragment newInstance() {
        RouterInfoFragment fragment = new RouterInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_router_info, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Router Info");

        refreshLayout=rootView.findViewById(R.id.info_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadInfo());

        lanipRow=rootView.findViewById(R.id.lan_ip_row);
        networkMaskRow=rootView.findViewById(R.id.network_mask_row);
        imeiRow=rootView.findViewById(R.id.imei_row);
        macRow=rootView.findViewById(R.id.mac_row);
        runtimeRow=rootView.findViewById(R.id.runtime_row);

        lanipTV=rootView.findViewById(R.id.lanip_tv);
        networkMaskTV=rootView.findViewById(R.id.network_mask_tv);
        imeiTV=rootView.findViewById(R.id.imei_tv);
        macTV=rootView.findViewById(R.id.mac_tv);
        runtimeTV=rootView.findViewById(R.id.runtime_tv);

        if (savedInstanceState==null)
            loadInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    private void loadInfo(){
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (compositeDisposable != null) compositeDisposable.dispose();

        Disposable worker1 = Single.fromCallable(() -> (new Api()).getCellNetworkInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final CellNetworkInfo result) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });


        Disposable worker2 = Single.fromCallable(() -> (new Api()).getRuntimeInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final RuntimeInfo result) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });

        Disposable worker3 = Single.fromCallable(() -> (new Api()).getWiFiInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final WiFi result) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });

        Disposable worker4 = Single.fromCallable(() -> (new Api()).getLANIPInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final LANIPInfo result) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    if (completionCounter.incrementAndGet() == 4) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.addAll(worker1, worker2, worker3, worker4);
    }

    private void handleResult(LANIPInfo info){
        lanipTV.setText(info.getLanIP());
        networkMaskTV.setText(info.getNetworkMask());

        lanipRow.setVisibility(View.VISIBLE);
        networkMaskRow.setVisibility(View.VISIBLE);
    }

    private void handleResult(CellNetworkInfo info){
        imeiTV.setText(info.getIMEI());

        imeiRow.setVisibility(View.VISIBLE);
    }

    private void handleResult(WiFi info){
        macTV.setText(info.getMAC());

        macRow.setVisibility(View.VISIBLE);
    }

    private void handleResult(RuntimeInfo info){
        runtimeTV.setText(info.getPrettiedTime());

        runtimeRow.setVisibility(View.VISIBLE);
    }
}
