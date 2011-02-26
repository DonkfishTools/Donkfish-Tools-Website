package com.donkfish.tools.client.tools;

import com.donkfish.core.client.model.Tool;
import com.donkfish.core.client.tools.ToolsManager;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ToolWidgetFactor {

    public static Widget getWidget(String toolKey)
    {
        if(toolKey.equals(ToolsManager.TOOL_KEY_WORD_COUNT) || toolKey.equals(ToolsManager.TOOL_KEY_CHAR_COUNT))
                return new TextCountTool();

        return new HTML("No tool found");


    }
}
