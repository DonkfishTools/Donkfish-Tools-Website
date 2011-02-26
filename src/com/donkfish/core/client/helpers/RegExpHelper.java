package com.donkfish.core.client.helpers;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;

public class RegExpHelper {
    public static String[] getAllMatches(String pattern, String input)
    {
        ArrayList<String> matches = new ArrayList<String>();
        RegExp regex = RegExp.compile(pattern, "gi");

        MatchResult result = regex.exec(input);
        while(result != null)
        {
            matches.add(result.getGroup(0));
            result = regex.exec(input);
        }

        return matches.toArray(new String[matches.size()]);
    }
}
