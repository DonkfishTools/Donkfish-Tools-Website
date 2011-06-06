package com.donkfish.tools.client.tools.casing;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.donkfish.core.client.services.DonkfishService;
import com.donkfish.tools.client.tools.base.SpringTextBoxTool;
import com.donkfish.tools.client.tools.results.ResultsPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;

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
    protected void updateStats(String text, ResultsPanel statsPanel, final TextArea textArea) {
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
                                        DonkfishService.sendCommand(new Command(Command.TITLECASE, text), new AsyncCallback<CommandResult>(){
                                            public void onFailure(Throwable throwable) {
                                                GWT.log("Failed case tool", throwable);
                                            }

                                            public void onSuccess(CommandResult commandResult) {
                                                textArea.setText(toTitleCase(commandResult.getPayLoad()));
                                            }
                                        });

                break;
                                                case Proper:
                                                    DonkfishService.sendCommand(new Command(Command.PROPERCASE, text), new AsyncCallback<CommandResult>(){
                                                                                                public void onFailure(Throwable throwable) {
                                                                                                    GWT.log("Failed case tool", throwable);
                                                                                                }

                                                                                                public void onSuccess(CommandResult commandResult) {
                                                                                                    textArea.setText(toTitleCase(commandResult.getPayLoad()));
                                                                                                }
                                                                                            });

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
