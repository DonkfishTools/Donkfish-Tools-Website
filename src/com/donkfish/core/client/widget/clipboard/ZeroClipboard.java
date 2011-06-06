package com.donkfish.core.client.widget.clipboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class ZeroClipboard extends Composite {

    String clipContainerId = "clipContainerId" + Math.round(Math.random() * 1000);
    String clipButtonId = "clipButtonId" + Math.round(Math.random() * 1000);
    JavaScriptObject clip;
    private String str = "";

    public ZeroClipboard(String text) {

        HTML html = new HTML("<div id=\"" + clipContainerId + "\" style=\"position:relative\"> <div id=\"" + clipButtonId + "\" class=\"my_clip_button\">"+ text + "</div> </div>");
        html.addStyleName(RES.styles().mainHtmlBlock());
        initWidget(html);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand(){
            public boolean execute() {
                clip = glue(clipContainerId, clipButtonId);
                setText(str);
                return false;
            }
        }, 1000);

    }

    public void setText(String str)
    {
        this.str = str;
        if(clip != null)
            setTextCore(clip, str);
    }

    private native void setTextCore(JavaScriptObject clip, String str) /*-{
        clip.setText(str);
    }-*/;


    private native JavaScriptObject glue(String contianerId, String buttonId) /*-{
        var clip = new $wnd.ZeroClipboard.Client();
        clip.setText( '' );
        clip.setHandCursor( true );
        clip.glue( buttonId, contianerId );
        return clip;
    }-*/;

    static interface Styles extends CssResource {


        String mainHtmlBlock();
    }

    static interface Resources extends ClientBundle {
        @Source("ZeroClipboard.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}