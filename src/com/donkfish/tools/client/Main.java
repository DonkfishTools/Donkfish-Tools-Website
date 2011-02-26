package com.donkfish.tools.client;

import com.donkfish.core.client.constants.HtmlVariables;
import com.donkfish.core.client.tools.ToolsManager;
import com.donkfish.tools.client.services.GreetingService;
import com.donkfish.tools.client.services.GreetingServiceAsync;
import com.donkfish.tools.client.tools.ToolWidgetFactor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {


  private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

  public void onModuleLoad() {
    RootPanel panel = RootPanel.get(HtmlVariables.ROOT_PANEL_MAIN);

      if(panel != null)
      {
          String toolKey = Document.get().getElementById(HtmlVariables.KEY).getAttribute("value").toString();
          panel.add(ToolWidgetFactor.getWidget(toolKey));
      }
      else
      {
          Window.alert("No root panel");
      }
  }
}
