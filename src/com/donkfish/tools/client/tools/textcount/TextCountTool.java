package com.donkfish.tools.client.tools.textcount;

import com.donkfish.core.client.helpers.RegExpHelper;
import com.donkfish.core.client.helpers.StringCounter;
import com.donkfish.tools.client.tools.SpringTextBoxTool;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TextCountTool extends SpringTextBoxTool {

    @Override
    protected void updateStats(String text, VerticalPanel statsPanel, TextArea textArea) {

        statsPanel.clear();
        statsPanel.add(new HTML("How many letter e's do you have: " + RegExpHelper.getAllMatches("e", text).length));
        statsPanel.add(new HTML("Char Count w/ Spaces: " + RegExpHelper.getAllMatches(".", text).length));
        statsPanel.add(new HTML("Char Count w/o Spaces: " + RegExpHelper.getAllMatches("[^\\s]", text).length));
        statsPanel.add(new HTML("Letter Count: " + RegExpHelper.getAllMatches("[a-zA-Z]", text).length));
        statsPanel.add(new HTML("Digit Count: " + RegExpHelper.getAllMatches("[0-9]", text).length));
        String[] words = RegExpHelper.getAllMatches("\\b(\\d+(,|\\.)?)+|([a-z]\\.)+|[\\w']+\\b", text);

        StringCounter counter = new StringCounter(words);

        statsPanel.add(new HTML("Word Count: " + counter.getTotal()));
        statsPanel.add(new HTML("Unique Word Count: " + counter.getUnique()));
        statsPanel.add(new HTML("Vowel Count: " + RegExpHelper.getAllMatches("[aeiouy]", text).length));
        statsPanel.add(new HTML("Constant Count: " + RegExpHelper.getAllMatches("[bcdfghjklmnpqrstvwxz]", text).length));
        statsPanel.add(new HTML("Puncation Count: " + RegExpHelper.getAllMatches("[\\.\\?!@#\\$%\\^&*()`~:;{}\\[\\]/<>\\|\\\\\\-]", text).length));


        VerticalPanel wordPanel = new VerticalPanel();

        statsPanel.add(wordPanel);

        for(StringCounter.StringCountValue value : counter.getValues())
        {
            wordPanel.add(new HTML(value.toString() + " :: " + value.getCount()));
        }




    }

    static interface Styles extends CssResource {


    }

    static interface Resources extends ClientBundle {
        @Source("TextCountTool.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}