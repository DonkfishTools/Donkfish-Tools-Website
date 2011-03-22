package com.donkfish.tools.client.tools.numbers;

import com.donkfish.tools.client.tools.SpringTextBoxTool;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Collections;

public class NumberListTool extends SpringTextBoxTool {

    @Override
    protected void updateStats(String text, VerticalPanel statsPanel, TextArea textArea) {

        statsPanel.clear();

        String[] numbers = text.split("\\s");
        ArrayList<Double> reals = new ArrayList<Double>();

        for(String number : numbers)
        {
            if(number == null || number.isEmpty())
                continue;
            
            reals.add(Double.parseDouble(number));
        }



        double sum = 0.0;
        double mean = 0.0;
        double median = 0.0;
        double mode = 0.0;

        Collections.sort(reals);


        if(reals.size() > 0)
        {
            for(Double d : reals)
            {
                sum += d;

            }

            mean = sum / reals.size();
            median = reals.size() % 2 == 0 ? (reals.get((int)Math.ceil(reals.size() / 2)) - reals.get((int)Math.floor(reals.size() / 2))) + reals.get((int)Math.floor(reals.size() / 2)) : reals.get(reals.size() / 2);
        }

        statsPanel.add(new HTML("sum: " + sum));
        statsPanel.add(new HTML("count: " + reals.size()));
        statsPanel.add(new HTML("mean: " + mean));
        statsPanel.add(new HTML("median: " + median));
        statsPanel.add(new HTML("mode: " + mode));
        




    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("NumberListTool.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}