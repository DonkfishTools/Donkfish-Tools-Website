package com.donkfish.core.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("/donk")
public interface DonkfishRemoteService extends RemoteService {
  String greetServer(String name);
}
