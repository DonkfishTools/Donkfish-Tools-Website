package com.donkfish.tools.client.tools;

import com.donkfish.core.client.helpers.RegExpHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TextCountTool extends Composite {
    VerticalPanel mainPanel = new VerticalPanel();
    TextArea textArea;
    VerticalPanel statsPanel;

    public TextCountTool() {
        textArea = new TextArea();
        textArea.setHeight("300px");
        textArea.setWidth("500px");
        textArea.addStyleName(RES.styles().textArea());
        textArea.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                updateStats();
            }
        });
        textArea.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                if(textArea.getText().length() < 3000)
                    updateStats();
            }
        });
        mainPanel.add(textArea);

        statsPanel = new VerticalPanel();
        textArea.addStyleName(RES.styles().statsPanel());

        mainPanel.add(statsPanel);

        updateStats();

        initWidget(mainPanel);
    }

    private void updateStats() {
        String text = textArea.getText();
        statsPanel.clear();
        statsPanel.add(new HTML("Char Count w/ Spaces: " + RegExpHelper.getAllMatches(".", text).length));
        statsPanel.add(new HTML("Char Count w/o Spaces: " + RegExpHelper.getAllMatches("[^\\s]", text).length));
        statsPanel.add(new HTML("Letter Count w/ Spaces: " + RegExpHelper.getAllMatches("[a-zA-Z]", text).length));
        statsPanel.add(new HTML("Digit Count w/ Spaces: " + RegExpHelper.getAllMatches("[0-9]", text).length));
        statsPanel.add(new HTML("Word Count: " + RegExpHelper.getAllMatches("[^\\.\\?!@#\\$%\\^&*()`~:;{}\\[\\]/<>\\|\\\\+\\-\\s]", text).length));
        statsPanel.add(new HTML("Vowel Count: " + RegExpHelper.getAllMatches("[aeiouy]", text).length));
        statsPanel.add(new HTML("Constant Count: " + RegExpHelper.getAllMatches("[bcdfghjklmnpqrstvwxz]", text).length));
        statsPanel.add(new HTML("Puncation Count: " + RegExpHelper.getAllMatches("[\\.\\?!@#\\$%\\^&*()`~:;{}\\[\\]/<>\\|\\\\\\-]", text).length));

    }

    static interface Styles extends CssResource {

        String textArea();

        String statsPanel();
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