package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.Utils;
import com.lexoff.ergowrapper.info.TrafficStatisticsInfo;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StatisticsFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private View mainLayout;

    private TextView receivedData, sentData, completeData, errorData;
    private TextView allReceivedData, allSentData, allCompleteData, allErrorData;

    public StatisticsFragment() {
        //empty
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Statistics");

        refreshLayout=rootView.findViewById(R.id.stats_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadStatistics());

        mainLayout=rootView.findViewById(R.id.main_layout);

        receivedData=rootView.findViewById(R.id.received_data_tv);
        sentData=rootView.findViewById(R.id.sent_data_tv);
        completeData=rootView.findViewById(R.id.complete_data_tv);
        errorData=rootView.findViewById(R.id.error_data_tv);

        allReceivedData=rootView.findViewById(R.id.all_received_data_tv);
        allSentData=rootView.findViewById(R.id.all_sent_data_tv);
        allCompleteData=rootView.findViewById(R.id.all_complete_data_tv);
        allErrorData=rootView.findViewById(R.id.all_error_data_tv);

        if (savedInstanceState==null)
            loadStatistics();
    }

    private void loadStatistics(){
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getTrafficStatistics())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final TrafficStatisticsInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void handleResult(TrafficStatisticsInfo info){
        receivedData.setText(Utils.formatDataString(info.getReceivedBytes()));
        sentData.setText(Utils.formatDataString(info.getSentBytes()));
        completeData.setText(Utils.formatDataString(info.getCompleteBytes()));
        errorData.setText(Utils.formatDataString(info.getErrorBytes()));

        allReceivedData.setText(Utils.formatDataString(info.getAllReceivedBytes()));
        allSentData.setText(Utils.formatDataString(info.getAllSentBytes()));
        allCompleteData.setText(Utils.formatDataString(info.getAllCompleteBytes()));
        allErrorData.setText(Utils.formatDataString(info.getAllErrorBytes()));

        mainLayout.setVisibility(View.VISIBLE);
    }

}
