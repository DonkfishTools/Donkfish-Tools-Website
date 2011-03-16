package com.donkfish.tools.client.tools;

import com.donkfish.core.client.tools.ToolsManager;
import com.donkfish.tools.client.tools.html.TableGenerator;
import com.donkfish.tools.client.tools.pretty.CodePrettyTool;
import com.donkfish.tools.client.tools.textcount.TextCountTool;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ToolWidgetFactory {

    public static Widget getWidget(String toolKey)
    {
        if(toolKey.equals(ToolsManager.TOOL_KEY_WORD_COUNT) || toolKey.equals(ToolsManager.TOOL_KEY_CHAR_COUNT))
                return new TextCountTool();

        if(toolKey.equals(ToolsManager.TOOL_KEY_TABLE_CREATOR))
                return new TableGenerator();

        if(toolKey.equals(ToolsManager.TOOL_KEY_HTML_PRETTY))
                return new CodePrettyTool();

        return new HTML("No tool found");


    }
}
