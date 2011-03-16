package com.donkfish.core.client.helpers;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class OffscreenPanel extends SimplePanel {

    private OffscreenPanel() {
        super();
        RootPanel.get().add(this, -2000, -2000);
    }

    public static int measureHeight(Widget w) {
        OffscreenPanel panel = new OffscreenPanel();
        int height = 1;
        try
        {
            panel.setWidget(w);
            height = w.getOffsetHeight();
        }
        finally
        {
            panel.clear();
            panel.removeFromParent();
        }

        return height;
    }

    public static int measureWidth(Widget w) {
        OffscreenPanel panel = new OffscreenPanel();
        int width = 0;
        try
        {
            panel.setWidget(w);
            width = w.getOffsetWidth();
        }
        finally
        {
            panel.clear();
            panel.removeFromParent();
        }
        return width;
    }
}