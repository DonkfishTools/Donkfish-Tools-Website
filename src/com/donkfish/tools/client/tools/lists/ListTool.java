package com.donkfish.tools.client.tools.lists;

import com.donkfish.core.client.helpers.StringHelper;
import com.donkfish.tools.client.tools.base.ButtonContext;
import com.donkfish.tools.client.tools.base.SpringTextBoxTool;
import com.donkfish.tools.client.tools.base.TextButtonListTool;
import com.donkfish.tools.client.tools.results.ResultsPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.*;

import java.util.*;

public class ListTool extends TextButtonListTool {

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

    @Override
    protected ButtonContext[] getButtons() {
        return new ButtonContext[]{new ButtonContext("Sort", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                sort();
            }
        }),
        new ButtonContext("Randomize", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                randomize();
            }
        }),
        new ButtonContext("Reverse Order", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                reverse();
            }
        }),
        new ButtonContext("Remove Blanks", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                removeBlanks();
            }
        }),
        new ButtonContext("Remove Duplicates", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                removeDupes();
            }
        }),
        new ButtonContext("Trim Whitespace", new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                trim();
            }
        })};
    }

    private void trim() {
        String[] lines = getLines();

        for(int i = 0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
        }

        setlines(lines);
    }

    private void reverse() {
        String[] lines = getLines();
        Collections.reverse(Arrays.asList(lines));

        setlines(lines);
    }

    private void removeDupes() {
        String[] lines = getLines();
        HashSet<String> uniqueLines = new HashSet<String>();

        for(int i = 0; i < lines.length; i++)
        {
            uniqueLines.add(lines[i]);
        }

        setlines(uniqueLines.toArray(new String[uniqueLines.size()]));
    }

    private void removeBlanks() {
        String[] lines = getLines();
        ArrayList<String> actualLines = new ArrayList<String>();

        for(int i = 0; i < lines.length; i++)
        {
            if(!StringHelper.isNullOrEmpty(lines[i]))
                actualLines.add(lines[i]);
        }

        setlines(actualLines.toArray(new String[actualLines.size()]));
    }

    private void randomize() {
        String[] lines = getLines();
        lines = random(lines);
        setlines(lines);
    }

    private void sort() {
        String[] lines = getLines();
        Arrays.sort(lines);
        setlines(lines);
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