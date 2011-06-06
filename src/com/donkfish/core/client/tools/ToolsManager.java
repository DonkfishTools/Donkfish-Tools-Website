package com.donkfish.core.client.tools;

import com.donkfish.core.client.model.Tool;

import java.util.HashMap;

public class ToolsManager {
    public static final String TOOL_KEY_WORD_COUNT = "Word_Count";
    public static final String TOOL_KEY_CHAR_COUNT = "Char_Count";
    public static final String TOOL_KEY_LETTER_COUNT = "Letter_Count";
    public static final String TOOL_KEY_TABLE_CREATOR = "Table_Creator";
    public static final String TOOL_KEY_SUM_TOOL = "Sum";
    public static final String TOOL_KEY_MEAN_TOOL = "Mean";
    public static final String TOOL_KEY_MEDIAN_TOOL = "Median";
    public static final String TOOL_KEY_MODE_TOOL = "Mode";
    public static final String TOOL_KEY_SORT_TOOL = "Sort_List";
    public static final String TOOL_KEY_RANDOMIZE_TOOL = "Randomize_List";
    public static final String TOOL_KEY_UPPERCASE_TOOL = "Uppercase_Text";
    public static final String TOOL_KEY_LOWERCASE_TOOL = "Lowercase_Text";
    public static final String TOOL_KEY_BASE64_ENCODER_TOOL = "Base64_Encoder";
    public static final String TOOL_KEY_BASE64_DECODER_TOOL = "Base64_Decoder";
    public static final String TOOL_KEY_HTML_ENCODER = "Html_Encoder";
    public static final String TOOL_KEY_HTML_DECODER = "Html_Decoder";
    public static final String TOOL_KEY_XML_ENCODER = "Xml_Encoder";
    public static final String TOOL_KEY_XML_DECODER = "Xml_Decoder";
    public static final String TOOL_KEY_MD5_ENCRPTOR = "MD5_Encryptor";
    public static final String TOOL_KEY_SHA1_ENCRPTOR = "SHA1_Encryptor";
    public static final String TOOL_KEY_RANDOM_NUMBER = "Random_Number";
    public static final String TOOL_KEY_UUID = "UUID_Generator";
    public static final String TOOL_KEY_JSON_PRETTY = "Json_Pretty";
    public static final String TOOL_KEY_HTML_PRETTY = "Html_Pretty";
    public static final String TOOL_KEY_XML_PRETTY = "Xml_Pretty";
    public static final String TOOL_KEY_CSS_PRETTY = "Css_Pretty";
    public static final String TOOL_KEY_TITLECASE_TOOL = "Title_Case";
    public static final String TOOL_KEY_PROPERCASE_TOOL = "Proper_Case";
    public static final String TOOL_KEY_TIMER = "Timer";
    public static final String TOOL_KEY_STOP_WATCH = "Stop_Watch";


    public static HashMap<String, Tool> getTools() {
        initToolMap();
        return tools;
    }

    private static final HashMap<String, Tool> tools = new HashMap<String, Tool>();

    public static Tool getTool(String key)
    {
        initToolMap();
        return tools.get(key);
    }

    private static void initToolMap()
    {
        if(tools.size() == 0)
        {
            tools.put(TOOL_KEY_WORD_COUNT, new Tool("Word Count", "You can get the number of words", ""));
            tools.put(TOOL_KEY_CHAR_COUNT, new Tool("Char Count", "You can get the number of chars", ""));
            tools.put(TOOL_KEY_LETTER_COUNT, new Tool("Letter Count", "You can get the number of letters", ""));
//            tools.put(TOOL_KEY_TABLE_CREATOR, new Tool("Table Creator", "Create HTML quickly", ""));
//            tools.put(TOOL_KEY_SUM_TOOL, new Tool("Sum Tool", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_MEAN_TOOL, new Tool("Mean Tool", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_MEDIAN_TOOL, new Tool("Medium Tool", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_MODE_TOOL, new Tool("Mode Tool", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_SORT_TOOL, new Tool("Sort Tool", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_RANDOMIZE_TOOL, new Tool("Randomize Tool", "Add up all the numbers in a list", ""));
            tools.put(TOOL_KEY_UPPERCASE_TOOL, new Tool("Uppercase Text", "Add up all the numbers in a list", ""));
            tools.put(TOOL_KEY_LOWERCASE_TOOL, new Tool("Lowercase Text", "Add up all the numbers in a list", ""));
            tools.put(TOOL_KEY_TITLECASE_TOOL, new Tool("Title Text", "Add up all the numbers in a list", ""));
            tools.put(TOOL_KEY_PROPERCASE_TOOL, new Tool("Proper Text", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_BASE64_ENCODER_TOOL, new Tool("Base64 Encoder", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_BASE64_DECODER_TOOL, new Tool("Base64 Encoder", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_HTML_ENCODER, new Tool("HTML Encoder", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_HTML_DECODER, new Tool("HTML Decoder", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_XML_ENCODER, new Tool("XML Encoder", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_XML_DECODER, new Tool("XML Decoder", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_MD5_ENCRPTOR, new Tool("MD5 Ecrpytpr", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_SHA1_ENCRPTOR, new Tool("SHA-1 Encrypter", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_RANDOM_NUMBER, new Tool("Random Number Generator", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_UUID, new Tool("UUID Generator", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_JSON_PRETTY, new Tool("JSON Pretty Print", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_HTML_PRETTY, new Tool("HTML Pretty Print", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_XML_PRETTY, new Tool("XML Pretty Print", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_CSS_PRETTY, new Tool("CSS Pretty Print", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_TIMER, new Tool("Timer", "Add up all the numbers in a list", ""));
//            tools.put(TOOL_KEY_STOP_WATCH, new Tool("Stop Watch", "Add up all the numbers in a list", ""));
        }
    }
}
