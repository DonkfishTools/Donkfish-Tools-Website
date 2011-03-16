package com.donkfish.core.client.prettydiff;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;

public class PrettyDiff {
    public static native String pretty(String str) /*-{
        return $wnd.markup_beauty(str, 4, ' ', 'indent', true, false);
    }-*/;

    public static String encodeHTML(final String html)
    {
        HTML w = new HTML();
        w.setText(html);
        return w.getHTML();
    }
}
