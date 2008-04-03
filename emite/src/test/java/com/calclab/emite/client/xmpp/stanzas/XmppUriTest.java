package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;

public class XmppUriTest {

    @Test
    public void checkUriFormat() {
        final XmppURI uri = XmppURI.parseURI("xmpp:test@example/res");
        assertEquals("test", uri.getJid().getNode());
        assertEquals("example", uri.getJid().getHost());
        assertEquals("res", uri.getResource());
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyAtFails() {
        XmppURI.parseURI("xmpp:@");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyHostFails() {
        XmppURI.parseURI("xmpp:@example");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyNodeFails() {
        XmppURI.parseURI("xmpp:test@");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlySlashFails() {
        XmppURI.parseURI("xmpp:/");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutHost() {
        XmppURI.parseURI("xmpp:test@/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutHostFails() {
        XmppURI.parseURI("xmpp:test/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutNodeFails() {
        XmppURI.parseURI("xmpp:@example/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutResourceFails() {
        XmppURI.parseURI("xmpp:test@example");
    }

    @Test
    public void checkUriFormatWithoutXmpp() {
        XmppURI uri = XmppURI.parseURI("test@example/res");
        assertEquals("test", uri.getJid().getNode());
        assertEquals("example", uri.getJid().getHost());
        assertEquals("res", uri.getResource());
    }

    @Test
    public void checkEqualsUri() {
        XmppURI uri1 = XmppURI.parseURI("xmpp:test@example/res");
        XmppURI uri2 = XmppURI.parseURI("xmpp:test@example/res");
        assertEquals(uri1, uri2);
    }

    @Test
    public void checkHashMapUri() {
        XmppURI uri1 = XmppURI.parseURI("xmpp:test@example/res");
        XmppURI uri2 = XmppURI.parseURI("xmpp:test@example/res");
        assertEquals(uri1.hashCode(), uri2.hashCode());
    }

    @Test
    public void checkHashMapJid() {
        XmppURI uri1 = XmppURI.parseURI("xmpp:test@example/res");
        XmppURI uri2 = XmppURI.parseURI("xmpp:test@example/res");
        assertEquals(uri1.getJid().hashCode(), uri2.getJid().hashCode());
    }

    /*
     * Temporally here
     */
    @Test
    public void checkChatIdEquals() {
        XmppURI uri1 = XmppURI.parseURI("xmpp:test@example/res");
        XmppURI uri2 = XmppURI.parseURI("xmpp:test@example/res2");
        ChatId chatId1 = new ChatId(uri1.getJid());
        ChatId chatId2 = new ChatId(uri2.getJid());
        assertEquals(chatId1, chatId2);
    }

    /*
     * Temporally here
     */
    @Test
    public void checkChatIdHashCode() {
        XmppURI uri1 = XmppURI.parseURI("xmpp:test@example/res");
        XmppURI uri2 = XmppURI.parseURI("xmpp:test@example/res2");
        ChatId chatId1 = new ChatId(uri1.getJid());
        ChatId chatId2 = new ChatId(uri2.getJid());
        assertEquals(chatId1.hashCode(), chatId2.hashCode());
    }

}
