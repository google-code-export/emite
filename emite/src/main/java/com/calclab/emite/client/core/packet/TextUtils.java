package com.calclab.emite.client.core.packet;

public class TextUtils {
    /*
     * This method escape only some dangerous html chars
     */
    public static String escape(final String textOrig) {
	String text = textOrig;
	text = text.replaceAll("&", "&amp;");
	text = text.replaceAll("\"", "&quot;");
	// text = text.replaceAll("\'", "&#039;");
	text = text.replaceAll("<", "&lt;");
	text = text.replaceAll(">", "&gt;");
	return text;
    }

    /*
     * This method unescape only some dangerous html chars for use in GWT Html
     * widget for instance
     */
    public static String unescape(final String textOrig) {
	String text = textOrig;
	text = text.replaceAll("&amp;", "&");
	text = text.replaceAll("&quot;", "&quot;");
	text = text.replaceAll("&#039;", "\'");
	text = text.replaceAll("&lt;", "<");
	text = text.replaceAll("&gt;", ">");
	return text;
    }
}
