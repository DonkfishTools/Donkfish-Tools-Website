package com.donkfish.tools.client.tools.encrypt;

import com.donkfish.core.client.helpers.CommandHelper;
import com.donkfish.core.client.helpers.SpringHelper;
import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.services.DonkfishService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EncryptTool extends Composite {

    VerticalPanel mainPanel = new VerticalPanel();

    public EncryptTool() {

        final TextBox textBox = new TextBox();
        textBox.setWidth("300px");
        mainPanel.add(textBox);

        final TextBox textBoxMD5 = new TextBox();
        textBoxMD5.setWidth("300px");
        mainPanel.add(textBoxMD5);

        final TextBox textBoxSHA1 = new TextBox();
        textBoxSHA1.setWidth("300px");
        mainPanel.add(textBoxSHA1);

        SpringHelper.attachSpring(textBox, 400, new SpringHelper.SpringCallback(){
            public void run() {
                CommandHelper.sendCommand(Command.MD5, textBox.getText(), textBoxMD5);
                CommandHelper.sendCommand(Command.SHA1, textBox.getText(), textBoxSHA1);
            }

            public void activity() {
            }
        });


        initWidget(mainPanel);
    }

    static interface Styles extends CssResource {

   }

    static interface Resources extends ClientBundle {
        @Source("EncryptTool.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }
}
