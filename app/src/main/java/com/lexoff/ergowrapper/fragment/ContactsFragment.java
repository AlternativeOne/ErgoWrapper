package com.lexoff.ergowrapper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexoff.ergowrapper.Api;
import com.lexoff.ergowrapper.ContactsListAdapter;
import com.lexoff.ergowrapper.R;
import com.lexoff.ergowrapper.info.Contact;
import com.lexoff.ergowrapper.info.ContactsInfo;
import com.lexoff.ergowrapper.info.ResponseInfo;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactsFragment extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private ListView contactsListView;

    private int page=0;
    private int maxItemsPossible=0;

    public ContactsFragment() {
        //empty
    }

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        setHomeButton(true);
        setTitle("Contacts");

        refreshLayout=rootView.findViewById(R.id.contacts_refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            page=0;

            loadAllContacts();
        });

        contactsListView = rootView.findViewById(R.id.contacts_lv);
        contactsListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //empty
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (contactsListView.getAdapter() == null)
                    return;

                if (contactsListView.getAdapter().getCount() == 0)
                    return;

                if (contactsListView.getAdapter().getCount() >= maxItemsPossible)
                    return;

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount) {
                    loadMoreContacts();

                }
            }
        });

        contactsListView.setOnItemClickListener((parent, view, position, id) -> {
            loadContact(position);
        });

        contactsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu=new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.sms_list_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                Contact contact=(Contact) contactsListView.getAdapter().getItem(position);
                deleteContact(contact.getLocation(), contact.getIndex());

                return true;
            });
            popupMenu.show();

            return true;
        });

        if (savedInstanceState == null)
            loadAllContacts();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (currentWorker != null) {
            currentWorker.dispose();
            currentWorker = null;
        }
    }

    private void loadAllContacts() {
        refreshLayout.setRefreshing(false);

        if (isLoading.get()) return;

        refreshLayout.setRefreshing(true);

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getContactsInfo(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ContactsInfo result) -> {
                    isLoading.set(false);

                    handleResult(result);

                    refreshLayout.setRefreshing(false);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);

                    handleError(throwable);

                    refreshLayout.setRefreshing(false);
                });
    }

    private void loadMoreContacts(){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).getContactsInfo(page+=1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ContactsInfo result) -> {
                    isLoading.set(false);

                    handleItems(result);
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void handleResult(ContactsInfo info){
        maxItemsPossible=info.getContactsMax();

        ContactsListAdapter adapter = new ContactsListAdapter(requireContext(), info);
        contactsListView.setAdapter(adapter);
    }

    /*private void handleResult(UnreadSMSInfo info){
        new AlertDialog.Builder(requireContext())
                .setMessage(info.getUnreadCount()+" unread SMS")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }*/

    private void handleItems(ContactsInfo info){
        ContactsListAdapter adapter= (ContactsListAdapter) contactsListView.getAdapter();
        adapter.addItems(info.getContacts());
    }

    private void loadContact(int pos){
        ContactsListAdapter adapter=(ContactsListAdapter) contactsListView.getAdapter();

        handleResult((Contact) adapter.getItem(pos));
    }

    private void deleteContact(int location, int index){
        if (isLoading.get()) return;

        isLoading.set(true);

        if (currentWorker != null) currentWorker.dispose();

        currentWorker = Single.fromCallable(() -> (new Api()).deleteContact(location, index))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((@NonNull final ResponseInfo result) -> {
                    isLoading.set(false);

                    if (result.getSuccess()==1){
                        Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();

                        page=0;

                        loadAllContacts();
                    } else if (result.getSuccess()==0) {
                        Toast.makeText(requireContext(), "Operation unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }, (@NonNull final Throwable throwable) -> {
                    isLoading.set(false);
                    handleError(throwable);
                });
    }

    private void handleResult(Contact info){
        StringBuilder message=new StringBuilder();
        message.append("Mobile: ");
        message.append(info.getMobile());
        message.append("\n");

        if (info.hasHome()){
            message.append("Home: ");
            message.append(info.getHome());
            message.append("\n");
        }

        if (info.hasEmail()){
            message.append("Email: ");
            message.append(info.getDecodedEmail());
            message.append("\n");
        }

        if (info.hasOffice()){
            message.append("Office: ");
            message.append(info.getOffice());
            message.append("\n");
        }

        if (message.charAt(message.length()-1)=='\n') message.deleteCharAt(message.length()-1);

        new AlertDialog.Builder(requireContext())
                .setTitle(info.getDecodedName())
                .setMessage(message.toString())
                .setCancelable(true)
                .create()
                .show();
    }

}
