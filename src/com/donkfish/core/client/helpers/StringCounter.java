package com.donkfish.core.client.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StringCounter {

    private int total = 0;

    private StringCountValue[] values = null;

    public StringCounter(String[] strs)
    {
        total = strs.length;

        HashMap<String, Integer> tempValues = new HashMap<String, Integer>();

        for(String str : strs)
        {
            Integer value = tempValues.get(str);

            if(value == null)
                value = 0;

            value++;

            tempValues.put(str, value);
        }

        ArrayList<StringCountValue> countList = new ArrayList<StringCountValue>();
        for(String str : tempValues.keySet())
        {
            countList.add(new StringCountValue(str, tempValues.get(str)));
        }

        Collections.sort(countList);

        Collections.reverse(countList);

        values = countList.toArray(new StringCountValue[countList.size()]);



    }

    public int getTotal() {
        return total;
    }

    public StringCountValue[] getValues() {
        return values;
    }


    public class StringCountValue implements Comparable<StringCountValue>
    {
        public Integer count = 0;
        public String str = null;

        public StringCountValue(String str, Integer count) {
            this.count = count;
            this.str = str;
        }

        public int compareTo(StringCountValue stringCountValue) {
            return count.compareTo(stringCountValue.count);
        }

        @Override
        public String toString() {
            return str;
        }

        public Integer getCount() {
            return count;
        }
    }

}
