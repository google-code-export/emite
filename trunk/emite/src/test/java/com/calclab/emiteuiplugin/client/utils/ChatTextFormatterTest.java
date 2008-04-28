package com.calclab.emiteuiplugin.client.utils;

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
