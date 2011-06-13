package com.donkfish.server.services;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.donkfish.core.client.services.DonkfishRemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
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
        System.out.println(command.getMethod() + " :: " + command.getText());
        CommandResult result = processCommand(command);

        System.out.println(result.getPayLoad());
        return result;

    }

    private CommandResult processCommand(Command command) {
 if(command.getMethod() == Command.TITLECASE)
            return new CommandResult(WordUtils.capitalizeFully(StringUtils.lowerCase(command.getText())));
        else if(command.getMethod() == Command.PROPERCASE)
            return new CommandResult(getProperCase(command.getText()));
                else if(command.getMethod() == Command.ENCODE_XML)
            return new CommandResult(StringEscapeUtils.escapeXml(command.getText()));
                else if(command.getMethod() == Command.ENCODE_BASE64)
            return new CommandResult(new String(Base64.encodeBase64(command.getText().getBytes())));
                else if(command.getMethod() == Command.ENCODE_HTML)
            return new CommandResult(StringEscapeUtils.escapeHtml4(command.getText()));
                else if(command.getMethod() == Command.DECODE_XML)
            return new CommandResult(StringEscapeUtils.unescapeXml(command.getText()));
                else if(command.getMethod() == Command.DECODE_BASE64)
            return new CommandResult(new String(Base64.decodeBase64(command.getText().getBytes())));
                else if(command.getMethod() == Command.DECODE_HTML)
            return new CommandResult(StringEscapeUtils.unescapeHtml4(command.getText()));
                        else if(command.getMethod() == Command.ENCODE_JAVA)
            return new CommandResult(StringEscapeUtils.escapeJava(command.getText()));
                        else if(command.getMethod() == Command.DECODE_JAVA)
            return new CommandResult(StringEscapeUtils.unescapeJava(command.getText()));
                        else if(command.getMethod() == Command.ENCODE_JS)
            return new CommandResult(StringEscapeUtils.escapeEcmaScript(command.getText()));
                        else if(command.getMethod() == Command.DECODE_JS)
            return new CommandResult(StringEscapeUtils.unescapeEcmaScript(command.getText()));
                        else if(command.getMethod() == Command.ENCODE_CSV)
            return new CommandResult(StringEscapeUtils.escapeCsv(command.getText()));
                        else if(command.getMethod() == Command.DECODE_CSV)
            return new CommandResult(StringEscapeUtils.unescapeCsv(command.getText()));
        return null;
    }

    private String getProperCase(String text) {
        String lowerCaseText =  StringUtils.lowerCase(text);


        lowerCaseText = lowerCaseText.replace(">", "&great;");
        lowerCaseText = lowerCaseText.replace("<", "&less;");
        lowerCaseText = lowerCaseText.replace("*", "&star;");


        lowerCaseText = lowerCaseText.replaceAll("\\.\\s*", ">");
        lowerCaseText = lowerCaseText.replaceAll("!\\s*", "<");
        lowerCaseText = lowerCaseText.replaceAll("\\?\\s*", "*");

        lowerCaseText = WordUtils.capitalizeFully(lowerCaseText, '>', '<', '*');

        lowerCaseText = lowerCaseText.replace(">", ". ");
        lowerCaseText = lowerCaseText.replace("<", "! ");
        lowerCaseText = lowerCaseText.replace("*", "? ");

        lowerCaseText = lowerCaseText.replace("&great;", ">");
        lowerCaseText = lowerCaseText.replace("&less;", "<");
        lowerCaseText = lowerCaseText.replace("&star;", "*");

        lowerCaseText = lowerCaseText.replace(" i ", " I ");
        lowerCaseText = lowerCaseText.replace(" i'm ", " I'm ");
        lowerCaseText = lowerCaseText.replace(" i'll ", " I'll ");

        
        return lowerCaseText;
    }
}
