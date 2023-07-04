package com.lexoff.ergowrapper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

//mainly, util methods from Ergo web interface
public class Utils {
    public static String decodeSMSBody(String encodedBody){
        if (encodedBody==null || encodedBody.isEmpty()) return "";

        StringBuilder decoded=new StringBuilder();

        double strLen=encodedBody.length()/4;

        for (int idx=0; idx<strLen; idx++){
            int newIdx=idx*4;

            decoded.append((char)(Integer.parseInt(encodedBody.substring(newIdx, newIdx+4), 16)));
        }

        return decoded.toString();
    }

    public static String encodeBody(String str) {
        if (null == str || str.isEmpty()) {
            return "";
        }
        String code = "";
        for (int i = 0; i < str.length(); ++i) {
            String charCode = String.format("%x", (int)(str.charAt(i)));
            int paddingLen = 4 - charCode.length();
            for (int j = 0; j < paddingLen; ++j) {
                charCode = "0" + charCode;
            }
            code += charCode;
        }
        return code;
    }

    public static String decodeUSSDResponse(String encodedResponse){
        return decodeSMSBody(encodedResponse.replaceAll("\\s", "0"));
    }

    public static String formatDataString(long inbytes){
        if (inbytes<1024) return inbytes+" b";

        double bytes=inbytes/1024; //KB

        if (bytes<1024) return String.format("%.2f KB", bytes);

        bytes/=1024; //MB

        if (bytes<1024.0) return String.format("%.2f MB", bytes);

        bytes/=1024; //GB

        return String.format("%.2f GB", bytes);
    }

    public static String formatConnectionQuality(int rssi, int cellularSysNetworkMode, int cellularDataConnMode){
        if (0 == cellularSysNetworkMode) {
            return "0";
        } else if (1 == cellularSysNetworkMode) { //GSM 2G3G
            if (cellularDataConnMode != 1 && cellularDataConnMode != 2 && cellularDataConnMode != 16) {
                //3G

                if (rssi < 22)
                    return "0";
                else if (rssi < 27)
                    return "1";
                else if (rssi < 36)
                    return "2";
                else
                    return "3";
            } else {
                //2G

                if (rssi < 6)
                    return "0";
                else if (rssi < 12)
                    return "1";
                else if (rssi < 24)
                    return "2";
                else
                    return "3";
            }
        } else if (2 == cellularSysNetworkMode) { //LTE

            if (rssi < 21) {
                return "0";
            } else if (rssi < 31)
                return "1";
            else if (rssi < 41)
                return "2";
            else
                return "3";
        }

        return "0";
    }

    public static String formatSeconds(long time) {
        int d = 0;
        int h = 0;
        int m = 0;
        int s = 0;

        if (time < 60) {
            s = (int) time;
        } else if (time > 60 && time < 3600) {
            m = (int) (time / 60);
            s = (int) (time % 60);
        } else if (time >= 3600 && time < 86400) {
            h = (int) (time / 3600);
            m = (int) (time % 3600 / 60);
            s = (int) (time % 3600 % 60 % 60);
        } else if (time >= 86400) {
            d = (int) (time / 86400);
            h = (int) (time % 86400 / 3600);
            m = (int) (time % 86400 % 3600 / 60);
            s = (int) (time % 86400 % 3600 % 60 % 60);
        }

        return d + ":" + (h<10 ? "0"+h : h) + ":" + (m<10 ? "0"+m : m) + ":" + (s<10 ? "0"+s : s);
    }

    public static String formatChannel(int ch){
        return ch==0 ? "Auto" : ch+"";
    }

    public static String formatEncryption(String encr) {
        String authType;

        switch (encr) {
            //WPA-PSK
            case "psk+ccmp":
            case "psk+aes":
            case "psk":
            case "psk+tkip":
            case "psk+tkip+ccmp":
            case "psk+tkip+aes":
                authType = "WPA-PSK";
                break;
            //WPA2-PSK
            case "psk2":
            case "psk2+ccmp":
            case "psk2+aes":
            case "psk2+tkip+ccmp":
            case "psk2+tkip+aes":
            case "psk2+tkip":
                authType = "WPA2-PSK";
                break;
            //WPA/WPA2-MIXED
            case "psk-mixed":
            case "psk-mixed+aes":
            case "psk-mixed+ccmp":
            case "psk-mixed+tkip":
            case "psk-mixed+tkip+aes":
            case "psk-mixed+tkip+ccmp":
                authType = "WPA/WPA2 Mixed";
                break;
            //MEP
            case "wep":
            case "wep-open":
                authType = "MEP";
                break;
            case "none":
                authType = "None";
                break;
            default:
                authType = "Unknown";
                break;
        }

        return authType;
    }

    public static boolean isGSM7Code(String str) {
        int len = 0;
        for(int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if(((chr>=0x20&&chr<=0x7f)||0x20AC==chr||0x20AC==chr||0x0c==chr||0x0a==chr||0x0d==chr||0xa1==chr||0xa3==chr||0xa5==chr||0xa7==chr
                    ||0xbf==chr||0xc4==chr||0xc5==chr||0xc6==chr||0xc7==chr||0xc9==chr||0xd1==chr||0xd6==chr||0xd8==chr||0xdc==chr||0xdf==chr
                    ||0xe0==chr||0xe4==chr||0xe5==chr||0xe6==chr||0xe8==chr||0xe9==chr||0xec==chr||0xf11==chr||0xf2==chr||0xf6==chr||0xf8==chr||0xf9==chr||0xfc==chr
                    ||0x3c6==chr||0x3a9==chr||0x3a8==chr||0x3a3==chr||0x3a0==chr||0x39e==chr||0x39b==chr||0x398==chr||0x394==chr||0x393==chr)
                    && 0x60 != chr) {
                ++len;
            }
        }
        return len == str.length();
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("[0-9]+");
        if (pattern.matcher(phoneNumber).matches()) {
            return true;
        }
        else {
            return false;
        }
    }

    public static String getSmsTime() {
        Date date = new Date();
        String fullYear = date.getYear()+"";
        String year = fullYear.substring(1);
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        int timeZone = 0 - date.getTimezoneOffset() / 60;
        String monthStr=month+"";
        String dayStr=day+"";
        String hourStr=hour+"";
        String minuteStr=minute+"";
        String secondStr=second+"";
        String timeZoneStr = "";
        if (timeZone > 0) {
            timeZoneStr = "%2B" + timeZone;
        } else {
            timeZoneStr = "-" + timeZone;
        }
        if (month < 10) {
            monthStr = "0" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        }
        if (hour < 10) {
            hourStr = "0" +hour;
        }
        if (minute < 10) {
            minuteStr = "0" + minute;
        }
        if (second < 10) {
            secondStr = "0" + second;
        }
        String smsTime = year + "," + monthStr + "," + dayStr + "," + hourStr + "," + minuteStr + "," + secondStr + "," + timeZoneStr;
        return smsTime;
    }

    public static Fragment getLastFragment(FragmentManager manager){
        List<Fragment> fragments=manager.getFragments();

        return fragments.get(fragments.size()-1);
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void copyToClipboard(Context context, String label, String text) {
        try {
            ClipboardManager cManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cManager == null) return;

            ClipData clipData = ClipData.newPlainText(label, text);
            cManager.setPrimaryClip(clipData);

            Toast.makeText(context, "Copied to clipboard:\n"+text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error has happened"+(e.getMessage()==null ? "" : ":\n"+e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

}
