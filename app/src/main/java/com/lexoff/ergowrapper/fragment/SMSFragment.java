package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.SMSListAdapter;
import com.lexoff.ergowrapper.Utils;
import com.lexoff.ergowrapper.info.ResponseInfo;
import com.lexoff.ergowrapper.info.SMS;
import com.lexoff.ergowrapper.info.SMSInfo;
import com.lexoff.ergowrapper.info.UnreadSMSInfo;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SMSFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private Spinner typeSpinner;
    private ListView smsListView;

    private CompositeDisposable compositeDisposable;
    private AtomicInteger completionCounter=new AtomicInteger(0);

    private int type=Api.SMS_TYPE_INBOX;

    private int page=1;
    private int maxItemsPossible=0;

    public SMSFragment() {
        //empty
    }

    public static SMSFragment newInstance() {
        SMSFragment fragment = new SMSFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.sms_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.action_new_sms){
            showNewSMSDialog();
        } else {
            super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("SMS");

        refreshLayout=rootView.findViewById(R.id.sms_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            page=1;

            loadAllSMS();
        });

        typeSpinner=rootView.findViewById(R.id.sms_type_spinner);
        String[] items = new String[]{"Inbox", "SIM Inbox", "Sent", "Drafts"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                page=1;

                if (pos==0) type=Api.SMS_TYPE_INBOX;
                else if (pos==1) type=Api.SMS_TYPE_SIM_INBOX;
                else if (pos==2) type=Api.SMS_TYPE_SENT;
                else if (pos==3) type=Api.SMS_TYPE_DRAFTS;

                loadAllSMS();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                page=1;
                type=Api.SMS_TYPE_INBOX;
                loadAllSMS();
            }
        });

        smsListView = rootView.findViewById(R.id.sms_lv);
        smsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //empty
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = smsListView.getChildCount() == 0 ? 0 : smsListView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topRowVerticalPosition >= 0);

                if (smsListView.getAdapter() == null)
                    return;

                if (smsListView.getAdapter().getCount() == 0)
                    return;

                if (smsListView.getAdapter().getCount() >= maxItemsPossible)
                    return;

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount) {
                    loadMoreSMS();

                }
            }
        });

        smsListView.setOnItemClickListener((parent, view, position, id) -> {
            ((SMSListAdapter) smsListView.getAdapter()).setSMSRead(position);

            SMS sms=(SMS) smsListView.getAdapter().getItem(position);
            loadSMS(sms.getId());
        });

        smsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu=new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.sms_list_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                SMS sms=(SMS) smsListView.getAdapter().getItem(position);
                deleteSMS(sms.getId());

                return true;
            });
            popupMenu.show();

            return true;
        });

        if (savedInstanceState == null)
            loadAllSMS();
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

        if (currentWorker != null) {
            currentWorker.dispose();
            currentWorker = null;
        }
    }

    private void loadAllSMS() {
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (compositeDisposable != null) compositeDisposable.dispose();

        compositeDisposable=new CompositeDisposable();

        if (type==Api.SMS_TYPE_INBOX) {
            Disposable worker1 = Single.fromCallable(() -> (new Api()).getUnreadSMSInfo())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((@NonNull final UnreadSMSInfo result) -> {
                        if (completionCounter.incrementAndGet() == compositeDisposable.size()) {
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

            compositeDisposable.add(worker1);
        }

        Disposable worker2 = Single.fromCallable(() -> (new Api()).getSMSInfo(type, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final SMSInfo result) -> {
                    if (completionCounter.incrementAndGet() == compositeDisposable.size()) {
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

        compositeDisposable.add(worker2);
    }

    private void loadMoreSMS(){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getSMSInfo(type, page+=1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final SMSInfo result) -> {
                    isLoading.set(false);

                    handleItems(result);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void handleResult(SMSInfo info){
        maxItemsPossible=info.getSMSMax();

        SMSListAdapter adapter = new SMSListAdapter(requireContext(), info);
        smsListView.setAdapter(adapter);
    }

    private void handleResult(UnreadSMSInfo info){
        new AlertDialog.Builder(requireContext())
                .setMessage(info.getUnreadCount()+" unread SMS")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    private void handleItems(SMSInfo info){
        SMSListAdapter adapter= (SMSListAdapter) smsListView.getAdapter();
        adapter.addItems(info.getSMS());
    }

    private void loadSMS(int id){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getSMS(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final SMS result) -> {
                    isLoading.set(false);

                    handleResult(result);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void deleteSMS(int id){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).deleteSMS(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();

                        page=1;

                        loadAllSMS();
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void handleResult(SMS info){
        new AlertDialog.Builder(requireContext())
                .setTitle("From "+info.getAddresser())
                .setMessage(info.getDecodedBody())
                .setCancelable(true)
                .create()
                .show();
    }

    private void showNewSMSDialog() {
        View dialogView = View.inflate(requireContext(), R.layout.new_sms_dialog, null);
        EditText addressET = dialogView.findViewById(R.id.address_et);
        EditText bodyET = dialogView.findViewById(R.id.body_et);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("Send", null)
                .setNeutralButton("Save as draft", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button positiveButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String addresser = addressET.getText().toString();
                String body = bodyET.getText().toString();

                if (addresser.isEmpty() || body.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean bIsGSM7Code = Utils.isGSM7Code(body);

                if ((!bIsGSM7Code && body.length() > 335) || (bIsGSM7Code && body.length() > 765)) {
                    Toast.makeText(requireContext(), "Message is too long", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isValidNumber=false;

                if (addresser.contains(",")){
                    for (String phoneNumber : addresser.split(",")){
                        isValidNumber=Utils.isPhoneNumber(phoneNumber);

                        if (!isValidNumber) break;
                    }
                } else {
                    isValidNumber=Utils.isPhoneNumber(addresser);
                }

                if (!isValidNumber) {
                    Toast.makeText(requireContext(), "Invalid phonenumber", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!addresser.endsWith(",")) addresser+=",";

                sendSMS(-1, bIsGSM7Code ? 1 : 0, addresser, Utils.encodeBody(body), Utils.getSmsTime(),  0);

                dialog.dismiss();
            });

            Button neutralButton = ((AlertDialog) d).getButton(AlertDialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(v -> {
                String addresser = addressET.getText().toString();
                String body = bodyET.getText().toString();

                if (addresser.isEmpty() || body.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean bIsGSM7Code = Utils.isGSM7Code(body);

                //if ((!bIsGSM7Code && body.length() > 70) || (bIsGSM7Code && body.length() > 160)) {
                //    Toast.makeText(requireContext(), "Message is too long", Toast.LENGTH_SHORT).show();
                //    return;
                //}

                boolean isValidNumber=false;

                if (addresser.contains(",")){
                    for (String phoneNumber : addresser.split(",")){
                        isValidNumber=Utils.isPhoneNumber(phoneNumber);

                        if (!isValidNumber) break;
                    }
                } else {
                    isValidNumber=Utils.isPhoneNumber(addresser);
                }

                if (!isValidNumber) {
                    Toast.makeText(requireContext(), "Invalid phonenumber", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!addresser.endsWith(",")) addresser+=",";

                saveSMSDraft(-1, bIsGSM7Code ? 1 : 0, addresser, Utils.encodeBody(body), Utils.getSmsTime(), 2, 0);

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void saveSMSDraft(int id, int isGSM7, String address, String body, String date, int type, int protocol){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).saveSMSDraft(id, isGSM7, address, body, date, type, protocol))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        Toast.makeText(requireContext(), "Saved to Drafts", Toast.LENGTH_SHORT).show();

                        if (this.type==Api.SMS_TYPE_DRAFTS) {
                            page=1;

                            loadAllSMS();
                        }
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void sendSMS(int id, int isGSM7, String address, String body, String date, int protocol){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).sendSMS(id, isGSM7, address, body, date, protocol))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        Toast.makeText(requireContext(), "Sent", Toast.LENGTH_SHORT).show();

                        if (this.type==Api.SMS_TYPE_SENT) {
                            page=1;

                            loadAllSMS();
                        }
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

}
