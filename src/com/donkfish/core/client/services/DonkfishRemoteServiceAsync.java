package com.donkfish.core.client.services;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DonkfishRemoteServiceAsync {
  void greetServer(String input, AsyncCallback<String> callback);

    void sendCommand(Command command, AsyncCallback<CommandResult> asyncCallback);
}
