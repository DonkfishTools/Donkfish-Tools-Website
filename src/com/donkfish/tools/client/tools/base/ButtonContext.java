package com.donkfish.tools.client.tools.base;

import com.google.gwt.event.dom.client.ClickHandler;

public class ButtonContext {
    public String getName() {
        return name;
    }

    public ClickHandler getHandlenr() {
        return handlenr;
    }

    private String name;

    public ButtonContext(String name, ClickHandler handlenr) {
        this.name = name;
        this.handlenr = handlenr;
    }

    private ClickHandler handlenr;
}
