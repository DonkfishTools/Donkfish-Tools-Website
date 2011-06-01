package com.donkfish.tools.client.tools.textcount;

import com.donkfish.core.client.helpers.Analytics;
import com.donkfish.core.client.helpers.RegExpHelper;
import com.donkfish.core.client.helpers.StringCounter;
import com.donkfish.core.client.widget.clipboard.ZeroClipboard;
import com.donkfish.tools.client.tools.SpringTextBoxTool;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;


public class TextCountTool extends SpringTextBoxTool {

    @Override
    protected void updateStats(String text, VerticalPanel statsPanel, TextArea textArea) {


        statsPanel.clear();

        String[] words = RegExpHelper.getAllMatches("\\b(\\d+(,|\\.)?)+|([a-z]\\.)+|[\\w']+\\b", text);

        StringCounter counter = new StringCounter(words);

        addRow(statsPanel, "Word count", counter.getTotal());
        addRow(statsPanel, "Unique word count",  counter.getUnique());
        addRow(statsPanel, "Constant count", RegExpHelper.getAllMatches("[bcdfghjklmnpqrstvwxz]", text).length);
        addRow(statsPanel, "Puncation count", RegExpHelper.getAllMatches("[\\.\\?!@#\\$%\\^&*()`~:;{}\\[\\]/<>\\|\\\\\\-]", text).length);
        addRow(statsPanel, "Vowel count", RegExpHelper.getAllMatches("[aeiouy]", text).length);
        addRow(statsPanel, "Characters w/ spaces", RegExpHelper.getAllMatches(".", text).length);
        addRow(statsPanel, "Characters w/o spaces", RegExpHelper.getAllMatches("[^\\s]", text).length);
        addRow(statsPanel, "Letter count", RegExpHelper.getAllMatches("[a-zA-Z]", text).length);
        addRow(statsPanel, "Digit count", RegExpHelper.getAllMatches("[0-9]", text).length);

        VerticalPanel wordPanel = new VerticalPanel();

        statsPanel.add(wordPanel);

//        for(StringCounter.StringCountValue value : counter.getValues())
//        {
//            wordPanel.add(new HTML(value.toString() + " :: " + value.getCount()));
//        }


        Analytics.getInstance().trackEvent("tool", "text_count_tool", "process", counter.getTotal());


    }

    private void addRow(VerticalPanel statsPanel, String label, int value) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();

        horizontalPanel.setWidth("500px");

        HTML labelHTML = new HTML(label);
        labelHTML.addStyleName(RES.styles().label());
        horizontalPanel.add(labelHTML);
        horizontalPanel.setCellWidth(labelHTML, "320px");
        horizontalPanel.setCellVerticalAlignment(labelHTML, HasVerticalAlignment.ALIGN_MIDDLE);

        HTML valueHTML = new HTML(value + "");        
        horizontalPanel.add(valueHTML);
        horizontalPanel.setCellVerticalAlignment(valueHTML, HasVerticalAlignment.ALIGN_MIDDLE);

//        ZeroClipboard clipboardCopyButton = new ZeroClipboard();
//        clipboardCopyButton.setText(value + "");
//        horizontalPanel.add(clipboardCopyButton);
//        horizontalPanel.setCellHorizontalAlignment(clipboardCopyButton, HasHorizontalAlignment.ALIGN_RIGHT);
//        horizontalPanel.setCellVerticalAlignment(clipboardCopyButton, HasVerticalAlignment.ALIGN_MIDDLE);

        horizontalPanel.setHeight("30px");
        horizontalPanel.addStyleName(RES.styles().row());

        statsPanel.add(horizontalPanel);
    }

    @Override
    protected String getToolInstructions() {
        return "Simple paste your text into the box and let Donkfish do the rest.";
    }

    static interface Styles extends CssResource {


        String label();

        String row();
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