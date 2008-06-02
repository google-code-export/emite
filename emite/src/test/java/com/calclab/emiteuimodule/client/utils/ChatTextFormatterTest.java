package com.calclab.emiteuimodule.client.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ChatTextFormatterTest {

    @Test
    public void formatEmotiAfter() {
	final String message = ":) ";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertTrue(!format.equals(message));
    }

    @Test
    public void formatEmotiAlone() {
	final String message = ":)";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertTrue(!format.equals(message));
    }

    @Test
    public void formatEmotiEnd() {
	final String message = " :)";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertTrue(!format.equals(message));
    }

    @Test
    public void formatSpaceMultiEmoti() {
	final String message = ":) :) :)";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertTrue(!format.equals(message));
    }

    @Test
    public void formatURLs() {
	final String[] urls = new String[] { "http://emite.googlecode.com/", "ftp://debian.org/",
		"http://www.google.com/search?hl=es&rls=GGGL%2CGGGL%3A2006-28%2CGGGL%3Aes&q=emite&btnG=Buscar&lr=",
		"http://del.icio.us/search/?fr=del_icio_us&p=xmpp+gwt&type=all" };
	for (int i = 0; i < urls.length; i++) {
	    final String url = urls[i];
	    assertEquals("<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>", ChatTextFormatter.formatUrls(url));
	}
    }

    @Test
    public void onlySpaces() {
	final String message = "a:)a";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertEquals(format, message);
    }

    @Test
    public void preserveFtp() {
	final String message = "ftp://lalala";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertEquals(format, message);
    }

    @Test
    public void preserveHttp() {
	final String message = "http://lalala";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertEquals(format, message);
    }

    @Test
    public void preserveXml() {
	final String message = "<message from='room@rooms.domain/other' to='user@domain/resource' "
		+ "type='groupchat'><body>the message body</body></message>";
	final String format = ChatTextFormatter.preFormatEmoticons(message);
	assertEquals(format, message);
    }

}
