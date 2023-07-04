package com.lexoff.ergowrapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.lexoff.ergowrapper.fragment.BaseFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getWindow()!=null){
            getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }

        if (getSupportActionBar()!=null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DD571C")));
        }

        generateLogin();

        NavigationUtils.openNavigationFragment(this);
    }

    private void generateLogin(){
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        String username=prefs.getString("un", "");
        String password=prefs.getString("pw", "");

        LoginUtils.makeLoginUrl(username, password);
    }

    @Override
    public void onBackPressed() {
        Fragment lastFragment = Utils.getLastFragment(getSupportFragmentManager());

        if (lastFragment instanceof BaseFragment) {
            if (((BaseFragment) lastFragment).onBackPressed())
                return;
        }

        if (getSupportFragmentManager().getFragments().size()>1) getSupportFragmentManager().popBackStack();
        else this.finish();
    }

}
