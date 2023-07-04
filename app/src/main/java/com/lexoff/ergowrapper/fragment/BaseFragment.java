package com.lexoff.ergowrapper.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;

public class BaseFragment extends Fragment implements OnBackPressed {

    protected AtomicBoolean isLoading = new AtomicBoolean(false);
    protected Disposable currentWorker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id==android.R.id.home){
            requireActivity().onBackPressed();
        }

        return true;
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void setTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null && title != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setHomeButton(boolean show){
        Activity activity=requireActivity();
        if (activity!=null) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(show);
            }
        }
    }

    protected void handleError(Throwable e){
        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

}
