package com.donkfish.tools.client.tools.pretty;

import com.donkfish.core.client.codePrettify.CodePrettify;
import com.donkfish.core.client.helpers.SpringHelper;
import com.donkfish.core.client.prettydiff.PrettyDiff;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

public class CodePrettyTool extends Composite {
    VerticalPanel mainPanel = new VerticalPanel();
    TextArea textArea = null;
    HTML output = new HTML();



    public CodePrettyTool() {


        textArea = new TextArea();
        textArea.setHeight("300px");
        textArea.setWidth("500px");
        //textArea.addStyleName(RES.styles().textArea());

        SpringHelper.attachSpring(textArea, 400, new SpringHelper.SpringCallback() {
            public void run() {
                update();
            }

            public void activity() {

            }
        });

        mainPanel.add(textArea);
        mainPanel.add(output);

        initWidget(mainPanel);
    }

    private void update() {
        output.setHTML("<pre class=\"prettyprint\" >" + PrettyDiff.encodeHTML(PrettyDiff.pretty(textArea.getText())) + "</pre>");
        CodePrettify.pretty();
    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("CodePrettyTool.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}