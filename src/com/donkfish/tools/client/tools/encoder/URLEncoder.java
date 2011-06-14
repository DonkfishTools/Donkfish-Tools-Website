package com.donkfish.tools.client.tools.encoder;

import com.donkfish.core.client.helpers.CommandHelper;
import com.donkfish.core.client.helpers.SpringHelper;
import com.donkfish.core.client.model.Command;
import com.donkfish.tools.client.tools.base.SideToSideTextTool;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class URLEncoder extends SideToSideTextTool {
    @Override
    protected void moveRightToLeft(String text) {
        CommandHelper.sendCommand(Command.URL_DECODE, text, textAreaLeft);
    }

    @Override
    protected void moveLeftToRight(String text) {
        CommandHelper.sendCommand(Command.URL_ENCODE, text, textAreaRight);

    }
}