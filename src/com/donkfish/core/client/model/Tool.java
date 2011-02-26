package com.donkfish.core.client.model;

public class Tool {
    private String toolName;

    public String getToolName() {
        return toolName;
    }

    public String getToolDescriptionHtml() {
        return toolDescriptionHtml;
    }

    public String getSearchIndex() {
        return searchIndex;
    }

    private String toolDescriptionHtml;
    private String searchIndex;

    public Tool(String toolName, String toolDescriptionHtml, String searchIndex) {
        this.toolName = toolName;
        this.toolDescriptionHtml = toolDescriptionHtml;
        this.searchIndex = searchIndex;
    }


}
