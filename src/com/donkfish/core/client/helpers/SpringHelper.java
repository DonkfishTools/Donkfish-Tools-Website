package com.donkfish.core.client.helpers;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBoxBase;

public class SpringHelper {
    public interface SpringCallback
    {
        // The run action that should happen after the user has finished typing
        public void run();
        // notification that the user is typing
        public void activity();
    }

    final Timer timer = new Timer(){
        @Override
        public void run() {
            if (callback != null)
                callback.run();
        }
    };
    private TextBoxBase textBox;
    private int delay;
    private SpringCallback callback;
    String lastTextBoxValue = null;

    public SpringHelper(TextBoxBase textBox, int delay, SpringCallback callback) {


        this.textBox = textBox;
        this.delay = delay;
        this.callback = callback;

        textBox.addKeyUpHandler(getKeyUpHandler());
    }


    public KeyUpHandler getKeyUpHandler()
    {
        return new KeyUpHandler(){
        public void onKeyUp(KeyUpEvent keyPressEvent) {


                // Reset the timer for key presses
                final TextBoxBase textBox = (TextBoxBase)keyPressEvent.getSource();
                DeferredCommand.addPause();
                DeferredCommand.addCommand(new Command(){
                    public void execute() {

                        if(textBox.getText().equals(lastTextBoxValue))
                            return;

                        lastTextBoxValue = textBox.getText();
                        timer.cancel();
                        callback.activity();
                        timer.schedule(delay);
                    }
                });


            }
        };
    }


    public static SpringHelper attachSpring(final TextBoxBase textBox, final int delay, final SpringCallback callback)
    {
        return new SpringHelper(textBox, delay, callback);
    }
}
