package com.donkfish.tools.client.tools.lists;

import com.donkfish.tools.client.tools.base.SpringTextBoxTool;
import com.donkfish.tools.client.tools.results.ResultsPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.*;

import java.util.Arrays;

public class ListTool extends SpringTextBoxTool {

    @Override
    protected void updateStats(String text, ResultsPanel statsPanel, TextArea textArea) {
        statsPanel.clear();
        String[] values = text.split("\\s");

        Grid grid = new Grid(values.length, 2);

        Arrays.sort(values);

        for(int i = 0; i < values.length; i++)
        {
            grid.setWidget(i, 0, new HTML(values[i]));
        }

        values = random(values);
        for(int i = 0; i < values.length; i++)
        {
            grid.setWidget(i, 1, new HTML(values[i]));
        }

        statsPanel.add(grid);

    }

        public static <T> T[] random(T[] array)
    {
       for(int i = 0; i < array.length; i++)
       {
          int index = Random.nextInt(array.length - 1);
          T item = array[index];
          array[index]  = array[i];
          array[i] = item;
       }

        return array;
    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("ListTool.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}