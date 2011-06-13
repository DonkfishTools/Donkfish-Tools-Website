package com.donkfish.core.client.helpers;

import com.donkfish.core.client.model.Command;
import com.donkfish.core.client.model.CommandResult;
import com.donkfish.core.client.services.DonkfishService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBoxBase;

public class CommandHelper {
    public static void sendCommand(int method, String param, final TextBoxBase textBoxBase)
    {
        DonkfishService.sendCommand(new Command(method, param), new AsyncCallback<CommandResult>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(CommandResult commandResult) {
                textBoxBase.setText(commandResult.getPayLoad());

            }
        });
    }
}
