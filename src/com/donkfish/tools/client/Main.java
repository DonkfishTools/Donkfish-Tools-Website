package com.donkfish.tools.client;

import com.donkfish.core.client.constants.HtmlVariables;
import com.donkfish.core.client.services.DonkfishRemoteService;
import com.donkfish.core.client.services.DonkfishRemoteServiceAsync;
import com.donkfish.tools.client.tools.ToolWidgetFactory;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {


  private final DonkfishRemoteServiceAsync greetingService = GWT.create(DonkfishRemoteService.class);

  public void onModuleLoad() {
    RootPanel panel = RootPanel.get(HtmlVariables.ROOT_PANEL_MAIN);

      ExternalResources.instance.styles().ensureInjected();

      if(panel != null)
      {
          String toolKey = Document.get().getElementById(HtmlVariables.KEY).getAttribute("value").toString();
          panel.add(ToolWidgetFactory.getWidget(toolKey));
      }
      else
      {
          Window.alert("No root panel");
      }
  }
}
