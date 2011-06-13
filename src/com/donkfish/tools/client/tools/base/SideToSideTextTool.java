package com.donkfish.tools.client.tools.base;

import com.donkfish.core.client.helpers.SpringHelper;
import com.donkfish.core.client.helpers.StringHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

public abstract class SideToSideTextTool extends Composite {
    VerticalPanel mainPanel = new VerticalPanel();
    protected TextArea textAreaLeft;
    protected TextArea textAreaRight;

    public SideToSideTextTool() {

        String instructions = getToolInstructions();

        if(instructions != null)
        {
            HTML instructionHTML = new HTML(instructions);
            instructionHTML.addStyleName(BASE_RES.styles().instructions());
            mainPanel.add(instructionHTML);
        }

        HorizontalPanel textAreaPanel = new HorizontalPanel();

        textAreaLeft = new TextArea();
        textAreaLeft.setHeight("300px");
        textAreaLeft.setWidth("500px");
        textAreaLeft.addStyleName(BASE_RES.styles().textArea());

        textAreaRight = new TextArea();
        textAreaRight.setHeight("300px");
        textAreaRight.setWidth("500px");
        textAreaRight.addStyleName(BASE_RES.styles().textArea());

        SpringHelper.attachSpring(textAreaLeft, 400, new SpringHelper.SpringCallback() {
            public void run() {
                moveLeftToRight(textAreaLeft.getText());
            }

            public void activity() {

            }
        });

        SpringHelper.attachSpring(textAreaRight, 400, new SpringHelper.SpringCallback() {
            public void run() {
                moveRightToLeft(textAreaRight.getText());
            }

            public void activity() {

            }
        });

        textAreaPanel.add(textAreaLeft);
        textAreaPanel.add(textAreaRight);

        mainPanel.add(textAreaPanel);


        initWidget(mainPanel);
    }

    protected abstract void moveRightToLeft(String text);

    protected abstract void moveLeftToRight(String text);

    protected String getToolInstructions() {
        return null;
    }

   static interface Styles extends CssResource {

       String instructions();

       String textArea();
   }

    static interface Resources extends ClientBundle {
        @Source("SideToSideTextTool.css")
        Styles styles();

    }

    public static Resources BASE_RES;

    static {
        BASE_RES = (Resources) GWT.create(Resources.class);
        BASE_RES.styles().ensureInjected();
    }
}
