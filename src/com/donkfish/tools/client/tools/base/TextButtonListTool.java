package com.donkfish.tools.client.tools.base;

import com.donkfish.core.client.helpers.SpringHelper;
import com.donkfish.core.client.helpers.StringHelper;
import com.donkfish.tools.client.tools.results.ResultsPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

public abstract class TextButtonListTool extends Composite{

    VerticalPanel mainPanel = new VerticalPanel();
    TextArea textArea;

    public TextButtonListTool() {

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
        textArea.addStyleName(BASE_RES.styles().textArea());

        mainPanel.add(textArea);

        HorizontalPanel buttonPanel = new HorizontalPanel();

        for(ButtonContext context : getButtons())
        {
            Button button = new Button(context.getName());
            button.addClickHandler(context.getHandlenr());
            buttonPanel.add(button);
        }

        mainPanel.add(buttonPanel);

        initWidget(mainPanel);
    }

    protected void setlines(String[] lines) {
        textArea.setText(StringHelper.implode(lines, "\n"));
    }

    protected String[] getLines() {
        return textArea.getText().split("\n");
    }


    protected abstract ButtonContext[] getButtons();

    protected String getToolInstructions() {
        return null;
    }

   static interface Styles extends CssResource {

       String instructions();

       String textArea();
   }

    static interface Resources extends ClientBundle {
        @Source("TextButtonListTool.css")
        Styles styles();

    }

    public static Resources BASE_RES;

    static {
        BASE_RES = (Resources) GWT.create(Resources.class);
        BASE_RES.styles().ensureInjected();
    }
}
