package com.calclab.emiteuiplugin.client.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ChatTextFormatterTest {

    @Test
    public void formatEmotiAfter() {
        String message = ":) ";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertTrue(!format.equals(message));
    }

    @Test
    public void formatEmotiAlone() {
        String message = ":)";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertTrue(!format.equals(message));
    }

    @Test
    public void formatEmotiEnd() {
        String message = " :)";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertTrue(!format.equals(message));
    }

    @Test
    public void onlySpaces() {
        String message = "a:)a";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertEquals(format, message);
    }

    @Test
    public void preserveFtp() {
        String message = "ftp://lalala";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertEquals(format, message);
    }

    @Test
    public void preserveHttp() {
        String message = "http://lalala";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertEquals(format, message);
    }

    @Test
    public void preserveXml() {
        final String message = "<message from='room@rooms.domain/other' to='user@domain/resource' "
                + "type='groupchat'><body>the message body</body></message>";
        String format = ChatTextFormatter.preFormatEmoticons(message);
        assertEquals(format, message);
    }

}
