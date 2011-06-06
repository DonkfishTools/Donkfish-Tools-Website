package com.donkfish.server.services;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.donkfish.core.client.services.DonkfishRemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;


import java.net.URL;
import java.net.URLClassLoader;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DonkfishServiceImpl extends RemoteServiceServlet implements
        DonkfishRemoteService {

  public String greetServer(String input) {
    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");
    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }

    public CommandResult sendCommand(Command command) {
        if(command.getMethod() == Command.TITLECASE)
            return new CommandResult(WordUtils.capitalizeFully(StringUtils.lowerCase(command.getText())));
        else if(command.getMethod() == Command.PROPERCASE)
            return new CommandResult(WordUtils.capitalizeFully(StringUtils.lowerCase(command.getText()), '.', '!', '?'));
        return null;
    }
}
