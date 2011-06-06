package com.donkfish.core.client.services;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DonkfishService {
    private static DonkfishRemoteServiceAsync service = (DonkfishRemoteServiceAsync)GWT.create(DonkfishRemoteService.class);

    static {
                ServiceDefTarget endpoint = (ServiceDefTarget) service;
        endpoint.setServiceEntryPoint("/donk");
    }

    public static void getString()
    {
        service.greetServer("FOoo", new AsyncCallback<String>() {
            public void onFailure(Throwable throwable) {
            }

            public void onSuccess(String s) {
                Window.alert(s);
            }
        });
    }

    public static void sendCommand(Command command, AsyncCallback<CommandResult> asyncCallback) {
         service.sendCommand(command, asyncCallback);
    }
}
