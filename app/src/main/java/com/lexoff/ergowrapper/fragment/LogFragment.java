package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.info.LogInfo;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LogFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private TextView logTV;

    public LogFragment() {
        //empty
    }

    public static LogFragment newInstance() {
        LogFragment fragment = new LogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Logs");

        refreshLayout=rootView.findViewById(R.id.log_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadLogs());

        logTV=rootView.findViewById(R.id.log_tv);
        logTV.setMovementMethod(new ScrollingMovementMethod());

        if (savedInstanceState==null)
            loadLogs();
    }

    private void loadLogs(){
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getLogInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final LogInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void handleResult(LogInfo info){
        logTV.setText(info.getInvertedLogsAsString());
    }
}
