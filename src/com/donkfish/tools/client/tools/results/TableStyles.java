package com.donkfish.tools.client.tools.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public class TableStyles {
    static interface Styles extends CssResource {

        String mainTableStyle();

        String even();

        String odd();
    }

    static interface Resources extends ClientBundle {
        @Source("TableStyles.css")
        Styles styles();

        @ImageResource.ImageOptions(repeatStyle= ImageResource.RepeatStyle.Horizontal)
        ImageResource mainTableStyleGradient();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }
}
