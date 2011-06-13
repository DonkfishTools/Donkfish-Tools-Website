package com.donkfish.tools.client.tools.encoder;

import com.donkfish.core.client.helpers.CommandHelper;
import com.donkfish.core.client.model.Command;
import com.donkfish.tools.client.tools.base.SideToSideTextTool;

public class CsvEscapeTool extends SideToSideTextTool {
    @Override
    protected void moveRightToLeft(String text) {
        CommandHelper.sendCommand(Command.DECODE_CSV, text, textAreaLeft);
    }

    @Override
    protected void moveLeftToRight(String text) {
        CommandHelper.sendCommand(Command.ENCODE_CSV, text, textAreaRight);

    }
}