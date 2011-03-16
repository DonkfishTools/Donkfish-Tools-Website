package com.donkfish.core.client.widget.progress;

import com.donkfish.core.client.helpers.OffscreenPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;

public class LoadingSmall extends Composite {
    Grid mainPanel = new Grid(1,1);
    SimplePanel container = new SimplePanel();
    private Panel body;

    public LoadingSmall(Panel body) {
        this.body = body;

        body.setWidth("100%");
        body.setHeight("100%");

        mainPanel.getElement().getStyle().setZIndex(99999);
        mainPanel.setWidget(0, 0, new Image(RES.loading()));
        mainPanel.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

        mainPanel.setWidth("100%");
        mainPanel.setHeight("100%");

        mainPanel.setHeight(OffscreenPanel.measureHeight(body) + "px");

        showBody();

        initWidget(container);
    }

    public void showLoading()
    {
        container.setWidget(mainPanel);
    }

    public void showBody()
    {
        container.setWidget(body);
    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("LoadingSmall.css")
        Styles styles();

        ImageResource loading();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}