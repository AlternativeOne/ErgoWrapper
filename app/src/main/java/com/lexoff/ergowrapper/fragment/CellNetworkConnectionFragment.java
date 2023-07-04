package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.Utils;
import com.lexoff.ergowrapper.info.CellNetworkInfo;
import com.lexoff.ergowrapper.info.ResponseInfo;
import com.lexoff.ergowrapper.info.WANConfigInfo;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CellNetworkConnectionFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private CompositeDisposable compositeDisposable;
    private AtomicInteger completionCounter=new AtomicInteger(0);

    private View connectionQualityRow;
    private View connectionStatusRow;
    private View switchCNWStateRow;

    private TextView connectionQualityTV;
    private TextView connectionStatusTV;

    private Button switchCNWStateButton;
    private boolean isCNWEnabled;

    public CellNetworkConnectionFragment() {
        //empty
    }

    public static CellNetworkConnectionFragment newInstance() {
        CellNetworkConnectionFragment fragment = new CellNetworkConnectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cell_network_connection, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Cell network connection");

        refreshLayout=rootView.findViewById(R.id.cnc_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadInfo());

        connectionQualityRow=rootView.findViewById(R.id.connection_quality_row);
        connectionStatusRow=rootView.findViewById(R.id.connection_status_row);
        switchCNWStateRow=rootView.findViewById(R.id.switch_cnw_state_row);

        connectionQualityTV=rootView.findViewById(R.id.connection_quality_tv);
        connectionStatusTV=rootView.findViewById(R.id.connection_status_tv);

        switchCNWStateButton=rootView.findViewById(R.id.switch_cnw_state_btn);
        switchCNWStateButton.setOnClickListener(v -> {
            if (isCNWEnabled) {
                switchCNWState("cellular", "disabled");
            } else {
                switchCNWState("cellular", "cellular");
            }
        });

        if (savedInstanceState==null)
            loadInfo();
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
                    if (completionCounter.incrementAndGet() == 2) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });

        Disposable worker2 = Single.fromCallable(() -> (new Api()).getWANConfigInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final WANConfigInfo result) -> {
                    if (completionCounter.incrementAndGet() == 2) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    if (completionCounter.incrementAndGet() == 2) {
                        completionCounter.set(0);

                        isLoading.set(false);
                    }

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.addAll(worker1, worker2);
    }

    protected void switchCNWState(String proto, String dial_switch){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getSwitchCellNWStateInfo(proto, dial_switch))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void handleResult(CellNetworkInfo info){
        connectionQualityTV.setText(info.getConnectionQuality());
        connectionStatusTV.setText(info.getConnectionStatus());

        connectionQualityRow.setVisibility(View.VISIBLE);
        connectionStatusRow.setVisibility(View.VISIBLE);
    }

    private void handleResult(WANConfigInfo info){
        if (info.getDialSwitch().equals("disabled")){
            isCNWEnabled=false;

            switchCNWStateButton.setText("Enable");
        } else {
            isCNWEnabled=true;

            switchCNWStateButton.setText("Disable");
        }

        switchCNWStateRow.setVisibility(View.VISIBLE);
    }

    private void handleResult(ResponseInfo info){
        if (info.getSuccess()==1){
            loadInfo();
        } else if (info.getSuccess()==0) {
            Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}
