package com.lexoff.ergowrapper;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lexoff.ergowrapper.fragment.CellNetworkConnectionFragment;
import com.lexoff.ergowrapper.fragment.ConnectedDevicesFragment;
import com.lexoff.ergowrapper.fragment.ContactsFragment;
import com.lexoff.ergowrapper.fragment.InternetConnectionFragment;
import com.lexoff.ergowrapper.fragment.LogFragment;
import com.lexoff.ergowrapper.fragment.NavigationFragment;
import com.lexoff.ergowrapper.fragment.RouterInfoFragment;
import com.lexoff.ergowrapper.fragment.SMSFragment;
import com.lexoff.ergowrapper.fragment.StatisticsFragment;
import com.lexoff.ergowrapper.fragment.USSDFragment;
import com.lexoff.ergowrapper.fragment.WiFiNetworkFragment;

public class NavigationUtils {

    public static void openNavigationFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NavigationFragment navFragment = NavigationFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, navFragment);
        fragmentTransaction.commit();
    }

    public static void openSmsFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SMSFragment smsFragment = SMSFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, smsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openContactsFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ContactsFragment contactsFragment = ContactsFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, contactsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openStatisticsFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        StatisticsFragment statsFragment = StatisticsFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, statsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openUSSDFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        USSDFragment ussdFragment = USSDFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, ussdFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openRouterInfoFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RouterInfoFragment infoFragment = RouterInfoFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, infoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openCNCFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CellNetworkConnectionFragment cncFragment = CellNetworkConnectionFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, cncFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openICFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        InternetConnectionFragment icFragment = InternetConnectionFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, icFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openConnectionDevicesFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ConnectedDevicesFragment cdFragment = ConnectedDevicesFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, cdFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openWiFiNetworkFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WiFiNetworkFragment wnFragment = WiFiNetworkFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, wnFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

    public static void openLogFragment(Activity activity){
        FragmentManager fragmentManager = ((MainActivity) activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LogFragment logFragment = LogFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_container, logFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(Utils.getLastFragment(fragmentManager));
        fragmentTransaction.commit();
    }

}
