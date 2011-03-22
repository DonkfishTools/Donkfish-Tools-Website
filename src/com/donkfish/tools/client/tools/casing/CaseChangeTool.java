package com.donkfish.tools.client.tools.casing;

import com.donkfish.tools.client.tools.SpringTextBoxTool;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CaseChangeTool extends SpringTextBoxTool {

    private CaseType caseType;

    public enum CaseType {
        Upper,
        Lower,
        Title,
        Proper
    }

    public CaseChangeTool(CaseType caseType) {
        super();
        this.caseType = caseType;
    }

    @Override
    protected void updateStats(String text, VerticalPanel statsPanel, TextArea textArea) {
        if(text == null || caseType == null)
            return;
        
        switch(caseType)
        {
            case Upper:
                textArea.setText(text.toUpperCase());
                break;
                        case Lower:
                textArea.setText(text.toLowerCase());
                break;
                                    case Title:
                textArea.setText(toTitleCase(text));
                break;
                                                case Proper:
                textArea.setText(toProperCase(text));
                break;
        }

    }

    private String toProperCase(String text) {
        return text;
    }

    private String toTitleCase(String text) {
        return text;
    }
}
