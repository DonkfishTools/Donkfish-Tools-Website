package com.donkfish.core.client.helpers;

public class StringHelper {

    public static <T> String implode(T[] strings, String delim) {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < strings.length; i++) {
            out.append(strings[i]);
            if(i != strings.length - 1 )
                out.append(delim);
        }
        return out.toString();
    }


    public static boolean isNullOrEmpty(String string)
    {
        return string == null || string.trim().length() == 0;
    }

}
