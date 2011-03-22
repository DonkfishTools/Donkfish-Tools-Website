package com.donkfish.core.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DonkfishRemoteServiceAsync {
  void greetServer(String input, AsyncCallback<String> callback);
}
