package com.donkfish.tools.client.tools.encoder;

import com.donkfish.core.client.helpers.CommandHelper;
import com.donkfish.core.client.model.Command;
import com.donkfish.tools.client.tools.base.SideToSideTextTool;

public class JavaEscapeTool extends SideToSideTextTool {
    @Override
    protected void moveRightToLeft(String text) {
        CommandHelper.sendCommand(Command.DECODE_JAVA, text, textAreaLeft);
    }

    @Override
    protected void moveLeftToRight(String text) {
        CommandHelper.sendCommand(Command.ENCODE_JAVA, text, textAreaRight);

    }
}