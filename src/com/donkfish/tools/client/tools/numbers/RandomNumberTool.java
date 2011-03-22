package com.donkfish.tools.client.tools.numbers;

import com.donkfish.core.client.services.DonkfishService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

public class RandomNumberTool extends Composite {
    VerticalPanel mainPanel = new VerticalPanel();
    VerticalPanel outputPanel = new VerticalPanel();
            TextBox minInput = new TextBox();
        TextBox maxInput = new TextBox();

    public RandomNumberTool() {

        HorizontalPanel inputPanel = new HorizontalPanel();


        minInput.setText("0");
        maxInput.setText("100");
        inputPanel.add(minInput);
        inputPanel.add(maxInput);
        mainPanel.add(inputPanel);

        Button randomButton = new Button();
        randomButton.setText("Random");
        randomButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                update();

            }
        });
        mainPanel.add(randomButton);

        mainPanel.add(outputPanel);



        update();

        initWidget(mainPanel);
    }

    private void update() {
        outputPanel.clear();
        outputPanel.add(random(Integer.parseInt(minInput.getText()), Integer.parseInt(maxInput.getText())));

        outputPanel.add(new HTML("Standard Ranges:"));
        outputPanel.add(random(0, 1));
        outputPanel.add(random(0, 10));
        outputPanel.add(random(0, 100));
        outputPanel.add(random(0, 1000000));
    }

    private Widget random(int min, int max) {
        return new HTML("Range (" + min + ", " + max + ") : " + (int)(((Math.random() * (max - min)) + 0.5) + min));

    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("RandomNumberTool.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}