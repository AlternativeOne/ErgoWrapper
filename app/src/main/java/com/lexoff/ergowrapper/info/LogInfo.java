package com.lexoff.ergowrapper.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LogInfo extends Info {

    private ArrayList<String> logs;

    public LogInfo(){
        logs=new ArrayList<>();
    }

    public ArrayList<String> getLogs(){
        return logs;
    }

    public String getLogsAsString(){
        StringBuilder sb=new StringBuilder();

        for (String s : getLogs()){
            sb.append(s);
            sb.append("\n");
        }

        if (sb.charAt(sb.length()-1)=='\n') sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    public ArrayList<String> getInvertedLogs(){
        ArrayList<String> reversed=new ArrayList<>(logs);
        Collections.reverse(reversed);
        return reversed;
    }

    public String getInvertedLogsAsString(){
        StringBuilder sb=new StringBuilder();

        for (String s : getInvertedLogs()){
            sb.append(s);
            sb.append("\n");
        }

        if (sb.charAt(sb.length()-1)=='\n') sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    public void addLog(String log){
        logs.add(log);
    }

    public void addLogs(String[] logs){
        this.logs.addAll(Arrays.asList(logs));
    }

}
