package com.donkfish.tools.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface ExternalResources extends ClientBundle {

    public static final ExternalResources instance =  GWT.create(ExternalResources.class);

    @Source("externalStyle.css")
    ExternalStyles styles();
}
