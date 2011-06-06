package com.donkfish.tools.client.tools.base;

import com.donkfish.core.client.helpers.SpringHelper;
import com.donkfish.tools.client.tools.results.ResultsPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

public class SpringTextBoxTool extends Composite {
    VerticalPanel mainPanel = new VerticalPanel();
    TextArea textArea;
    ResultsPanel statsPanel = new ResultsPanel();

    public SpringTextBoxTool() {

        String instructions = getToolInstructions();

        if(instructions != null)
        {
            HTML instructionHTML = new HTML(instructions);
            instructionHTML.addStyleName(BASE_RES.styles().instructions());
            mainPanel.add(instructionHTML);
        }

        textArea = new TextArea();
        textArea.setHeight("300px");
        textArea.setWidth("500px");
        statsPanel.setWidth("100%");
        textArea.addStyleName(BASE_RES.styles().textArea());

        SpringHelper.attachSpring(textArea, 400, new SpringHelper.SpringCallback() {
            public void run() {
                updateStats(textArea.getText(), statsPanel, textArea);
                statsPanel.clearLoading();
            }

            public void activity() {
                statsPanel.showLoading();
            }
        });

        mainPanel.add(textArea);

        statsPanel.addStyleName(BASE_RES.styles().statsPanel());
        textArea.addStyleName(BASE_RES.styles().statsPanel());

        updateStats(textArea.getText(), statsPanel, textArea);


        mainPanel.add(statsPanel);
        initWidget(mainPanel);
    }

    protected String getToolInstructions() {
        return null;
    }

    protected void updateStats(String text, ResultsPanel statsPanel, TextArea textArea) {

    }

    static interface Styles extends CssResource {
        String textArea();

        String statsPanel();

        String instructions();
    }

    static interface Resources extends ClientBundle {
        @Source("SpringTextBoxTool.css")
        Styles styles();

    }

    public static Resources BASE_RES;

    static {
        BASE_RES = (Resources) GWT.create(Resources.class);
        BASE_RES.styles().ensureInjected();
    }

}