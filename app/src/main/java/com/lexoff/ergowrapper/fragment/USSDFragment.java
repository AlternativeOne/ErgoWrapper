package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.Utils;
import com.lexoff.ergowrapper.info.ResponseInfo;
import com.lexoff.ergowrapper.info.USSDResponseInfo;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class USSDFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private TextView responseTV;
    private EditText commandET;
    private Button sendCommandBtn;

    public USSDFragment() {
        //empty
    }

    public static USSDFragment newInstance() {
        USSDFragment fragment = new USSDFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ussd, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("USSD");

        refreshLayout=rootView.findViewById(R.id.ussd_refresh_layout);
        refreshLayout.setEnabled(false);

        responseTV=rootView.findViewById(R.id.response_tv);
        responseTV.setMovementMethod(new ScrollingMovementMethod());

        commandET=rootView.findViewById(R.id.command_et);

        sendCommandBtn=rootView.findViewById(R.id.send_command_btn);
        sendCommandBtn.setOnClickListener(v -> {
            String ussd=commandET.getText().toString();

            if (ussd.isEmpty()) {
                Toast.makeText(requireContext(), "Empty command", Toast.LENGTH_SHORT).show();

                return;
            }

            Utils.closeKeyboard(requireActivity());
            commandET.clearFocus();

            responseTV.setText(responseTV.getText()+ussd+" called. Response:\n\n");

            sendUSSD(ussd);
        });
    }

    private void sendUSSD(String ussd){
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).sendUSSD(ussd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        //Toast.makeText(requireContext(), "Operation successful", Toast.LENGTH_SHORT).show();

                        getUSSDResponse();
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();

                        refreshLayout.setRefreshing(false);
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void getUSSDResponse(){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getUSSDResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final USSDResponseInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void handleResult(USSDResponseInfo info){
        if (info.isNeedToRepeat()) {
            getUSSDResponse();
            return;
        }

        String response=info.getDecodedResponse();
        if (response.trim().isEmpty()){
            response="Operation unsuccessful.";
        }

        String text=responseTV.getText()+response;
        while (text.endsWith("\n")) text=text.substring(0, text.length()-1);
        text+="\n"+"-------------------------"+"\n";

        responseTV.setText(text);

        refreshLayout.setRefreshing(false);
    }

}
