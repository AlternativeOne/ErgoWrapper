package com.lexoff.ergowrapper.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.BuildConfig;
import com.lexoff.ergowrapper.LoginUtils;
import com.lexoff.ergowrapper.NavigationUtils;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.info.ResponseInfo;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NavigationFragment extends BaseFragment {

    protected AtomicBoolean isLoading = new AtomicBoolean(false);
    protected Disposable currentWorker;

    public NavigationFragment() {
        //empty
    }

    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.action_reboot){
            doReboot();
        } else if (id==R.id.action_poweroff){
            doPowerOff();
        } else if (id==R.id.action_login){
            showLoginDialog();
        } else if (id==R.id.action_about){
            showAboutDialog();
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        Button cncBtn=rootView.findViewById(R.id.go_cnc_btn);
        cncBtn.setOnClickListener(v -> NavigationUtils.openCNCFragment(requireActivity()));

        Button icBtn=rootView.findViewById(R.id.go_ic_btn);
        icBtn.setOnClickListener(v -> NavigationUtils.openICFragment(requireActivity()));

        Button wifiNetworkBtn=rootView.findViewById(R.id.go_wifi_network_btn);
        wifiNetworkBtn.setOnClickListener(v -> NavigationUtils.openWiFiNetworkFragment(requireActivity()));

        Button routerInfoBtn=rootView.findViewById(R.id.go_router_info_btn);
        routerInfoBtn.setOnClickListener(v -> NavigationUtils.openRouterInfoFragment(requireActivity()));

        Button connectedDevicesBtn=rootView.findViewById(R.id.go_cd_btn);
        connectedDevicesBtn.setOnClickListener(v -> NavigationUtils.openConnectionDevicesFragment(requireActivity()));

        Button smsBtn=rootView.findViewById(R.id.go_sms_btn);
        smsBtn.setOnClickListener(v -> NavigationUtils.openSmsFragment(requireActivity()));

        Button contactsBtn=rootView.findViewById(R.id.go_contacts_btn);
        contactsBtn.setOnClickListener(v -> NavigationUtils.openContactsFragment(requireActivity()));

        Button statsBtn=rootView.findViewById(R.id.go_stats_btn);
        statsBtn.setOnClickListener(v -> NavigationUtils.openStatisticsFragment(requireActivity()));

        Button logBtn=rootView.findViewById(R.id.go_log_btn);
        logBtn.setOnClickListener(v -> NavigationUtils.openLogFragment(requireActivity()));

        Button ussdBtn=rootView.findViewById(R.id.go_ussd_btn);
        ussdBtn.setOnClickListener(v -> NavigationUtils.openUSSDFragment(requireActivity()));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            setHomeButton(false);
            setTitle(getString(R.string.app_name));
        }
    }

    private void doReboot(){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).doReboot())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        Toast.makeText(requireContext(), "Operation successful. Router will be restarted in few minutes", Toast.LENGTH_SHORT).show();
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void doPowerOff(){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).doPowerOff())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        Toast.makeText(requireContext(), "Operation successful. Router will be powered off", Toast.LENGTH_SHORT).show();
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void showLoginDialog(){
        View dialogView = View.inflate(requireContext(), R.layout.login_dialog, null);
        EditText usernameET = dialogView.findViewById(R.id.username_et);
        EditText passwordET = dialogView.findViewById(R.id.password_et);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username1=prefs.getString("un", "");
        usernameET.setText(username1);
        String password1=prefs.getString("pw", "");
        passwordET.setText(password1);

        AlertDialog dialog=new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("Sign in", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button button = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                String username=usernameET.getText().toString();
                String password=passwordET.getText().toString();

                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginUtils.makeLoginUrl(username, password);

                prefs
                        .edit()
                        .putString("un", username)
                        .putString("pw", password)
                        .commit();

                Toast.makeText(requireContext(), "Done successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showAboutDialog(){
        String info="Version: "+ BuildConfig.VERSION_NAME+"\n"
                +"Developer: J57\n\n"
                +"This app uses Wi-Fi Router icon by Icons8 as app's logo.\n(https://icons8.com/icon/39474/wi-fi-router)";

        new AlertDialog.Builder(requireContext())
                .setMessage(info)
                .setCancelable(true)
                .setPositiveButton("Close", null)
                .create()
                .show();
    }

}
