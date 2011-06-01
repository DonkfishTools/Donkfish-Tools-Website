package com.donkfish.core.client.helpers;

public class Analytics {
    public static Analytics getInstance()
	{
		if (analytics == null)
			analytics = new Analytics();
		return analytics;
	}
	private static Analytics analytics;

	private Analytics()
	{
	}

	public native void trackEvent(String category, String action, String label, int value) /*-{
        if($wnd._gaq === null || typeof $wnd._gaq === 'undefined')
            return;
        $wnd._gaq.push(['_trackEvent', category, action, label, value]);
	}-*/ ;

	public native void trackPage(String pageUrl) /*-{
        if($wnd._gaq === null || typeof $wnd._gaq === 'undefined')
            return;
	 	$wnd._gaq.push(['_trackPageview', pageUrl]);
	}-*/ ;
}
