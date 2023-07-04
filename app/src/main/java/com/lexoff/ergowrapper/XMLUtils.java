package com.lexoff.ergowrapper;

public class XMLUtils {

    public static String getString(String xml, String tag){
        String openTag="<"+tag+">";
        String closeTag="</"+tag+">";

        int openIdx = xml.indexOf(openTag) + openTag.length();
        int closeIdx = xml.indexOf(closeTag);

        return xml.substring(openIdx, closeIdx);
    }

}
