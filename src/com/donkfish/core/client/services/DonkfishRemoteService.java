package com.donkfish.core.client.services;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("/donk")
public interface DonkfishRemoteService extends RemoteService {
  String greetServer(String name);

    CommandResult sendCommand(Command command);
}
