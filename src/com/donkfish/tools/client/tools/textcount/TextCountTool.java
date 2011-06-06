package com.donkfish.tools.client.tools.textcount;

import com.donkfish.core.client.helpers.Analytics;
import com.donkfish.core.client.helpers.RegExpHelper;
import com.donkfish.core.client.helpers.StringCounter;
import com.donkfish.tools.client.tools.base.SpringTextBoxTool;
import com.donkfish.tools.client.tools.results.ResultsPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;


public class TextCountTool extends SpringTextBoxTool {

    @Override
    protected void updateStats(String text, ResultsPanel statsPanel, TextArea textArea) {

        String[] words = RegExpHelper.getAllMatches("\\b(\\d+(,|\\.)?)+|([a-z]\\.)+|[\\w']+\\b", text);

        StringCounter counter = new StringCounter(words);

        statsPanel.putValue("Word count", counter.getTotal());
        statsPanel.putValue("Unique word count", counter.getValues().length);
        statsPanel.putValue("Constant count", RegExpHelper.getAllMatches("[bcdfghjklmnpqrstvwxz]", text).length);
        statsPanel.putValue("Punctuation count", RegExpHelper.getAllMatches("[\\.\\?!@#\\$%\\^&*()`~:;{}\\[\\]/<>\\|\\\\\\-]", text).length);
        statsPanel.putValue("Vowel count", RegExpHelper.getAllMatches("[aeiouy]", text).length);
        statsPanel.putValue("Characters w/ spaces", RegExpHelper.getAllMatches(".", text).length);
        statsPanel.putValue("Characters w/o spaces", RegExpHelper.getAllMatches("[^\\s]", text).length);
        statsPanel.putValue("Letter count", RegExpHelper.getAllMatches("[a-zA-Z]", text).length);
        statsPanel.putValue("Digit count", RegExpHelper.getAllMatches("[0-9]", text).length);

        Analytics.getInstance().trackEvent("tool", "text_count_tool", "process", counter.getTotal());
    }

    @Override
    protected String getToolInstructions() {
        return "Simple paste your text into the box and let Donkfish do the rest.";
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