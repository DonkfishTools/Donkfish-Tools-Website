package com.donkfish.tools.client.tools;

import com.donkfish.core.client.tools.ToolsManager;
import com.donkfish.tools.client.tools.casing.CaseChangeTool;
import com.donkfish.tools.client.tools.encoder.*;
import com.donkfish.tools.client.tools.encrypt.EncryptTool;
import com.donkfish.tools.client.tools.html.TableGenerator;
import com.donkfish.tools.client.tools.lists.ListTool;
import com.donkfish.tools.client.tools.numbers.NumberListTool;
import com.donkfish.tools.client.tools.numbers.RandomNumberTool;
import com.donkfish.tools.client.tools.pretty.CodePrettyTool;
import com.donkfish.tools.client.tools.textcount.TextCountTool;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ToolWidgetFactory {

    public static Widget getWidget(String toolKey)
    {
        if(toolKey.equals(ToolsManager.TOOL_KEY_WORD_COUNT) || toolKey.equals(ToolsManager.TOOL_KEY_CHAR_COUNT) || toolKey.equals(ToolsManager.TOOL_KEY_LETTER_COUNT))
                return new TextCountTool();

        if(toolKey.equals(ToolsManager.TOOL_KEY_TABLE_CREATOR))
                return new TableGenerator();

        if(toolKey.equals(ToolsManager.TOOL_KEY_HTML_PRETTY))
                return new CodePrettyTool();

                if(toolKey.equals(ToolsManager.TOOL_KEY_UPPERCASE_TOOL))
                return new CaseChangeTool(CaseChangeTool.CaseType.Upper);

                        if(toolKey.equals(ToolsManager.TOOL_KEY_LOWERCASE_TOOL))
                return new CaseChangeTool(CaseChangeTool.CaseType.Lower);

                        if(toolKey.equals(ToolsManager.TOOL_KEY_TITLECASE_TOOL))
                return new CaseChangeTool(CaseChangeTool.CaseType.Title);

                        if(toolKey.equals(ToolsManager.TOOL_KEY_PROPERCASE_TOOL))
                return new CaseChangeTool(CaseChangeTool.CaseType.Proper);

                                if(toolKey.equals(ToolsManager.TOOL_KEY_RANDOM_NUMBER))
                return new RandomNumberTool();

        if(toolKey.equals(ToolsManager.TOOL_KEY_UPPERCASE_TOOL) || toolKey.equals(ToolsManager.TOOL_KEY_LOWERCASE_TOOL) || toolKey.equals(ToolsManager.TOOL_KEY_TITLECASE_TOOL))
                return new CodePrettyTool();

        if(toolKey.equals(ToolsManager.TOOL_KEY_MEAN_TOOL) || toolKey.equals(ToolsManager.TOOL_KEY_MEAN_TOOL)|| toolKey.equals(ToolsManager.TOOL_KEY_MEDIAN_TOOL)|| toolKey.equals(ToolsManager.TOOL_KEY_MODE_TOOL)|| toolKey.equals(ToolsManager.TOOL_KEY_SUM_TOOL))
                return new NumberListTool();

        if(toolKey.equals(ToolsManager.TOOL_KEY_SORT_TOOL) || toolKey.equals(ToolsManager.TOOL_KEY_RANDOMIZE_TOOL))
                return new ListTool();

                if(toolKey.equals(ToolsManager.TOOL_KEY_BASE64_DECODER_TOOL) || toolKey.equals(ToolsManager.TOOL_KEY_BASE64_ENCODER_TOOL))
                return new Base64EncoderTool();

                        if(toolKey.equals(ToolsManager.TOOL_KEY_HTML_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_HTML_DECODER))
                return new HtmlEncoderTool();

                        if(toolKey.equals(ToolsManager.TOOL_KEY_XML_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_XML_DECODER))
                return new XmlEncoderTool();

                                if(toolKey.equals(ToolsManager.TOOL_KEY_CSV_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_CSV_DECODER))
                return new CsvEscapeTool();
                                if(toolKey.equals(ToolsManager.TOOL_KEY_JAVA_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_JAVA_DECODER))
                return new JavaEscapeTool();
                                if(toolKey.equals(ToolsManager.TOOL_KEY_JS_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_JS_DECODER))
                return new JSEscapeTool();

        if(toolKey.equals(ToolsManager.TOOL_KEY_MD5_ENCRPTOR) || toolKey.equals(ToolsManager.TOOL_KEY_SHA1_ENCRPTOR))
            return new EncryptTool();

                if(toolKey.equals(ToolsManager.TOOL_KEY_URL_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_URL_DECODER))
            return new URLEncoder();

                        if(toolKey.equals(ToolsManager.TOOL_KEY_REGEX_ENCODER) || toolKey.equals(ToolsManager.TOOL_KEY_REGEX_DECODER))
            return new RegexEncoder();


        return new HTML("No tool found");


    }
}
