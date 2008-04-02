package com.calclab.examplechat.client.chatuiplugin.modeltointegratewithemite;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmppUriSimpleTest {

    @Test
    public void checkUriFormat() {
        XmppUriSimple uri = new XmppUriSimple("xmpp:test@example/res");
        assertEquals("test", uri.getJid().getNode());
        assertEquals("example", uri.getJid().getHost());
        assertEquals("res", uri.getResource());
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutResourceFails() {
        new XmppUriSimple("xmpp:test@example");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutHostFails() {
        new XmppUriSimple("xmpp:test/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutNodeFails() {
        new XmppUriSimple("xmpp:@example/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyHostFails() {
        new XmppUriSimple("xmpp:@example");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyNodeFails() {
        new XmppUriSimple("xmpp:test@");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutHost() {
        new XmppUriSimple("xmpp:test@/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutXmpp() {
        new XmppUriSimple("test@example/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyAtFails() {
        new XmppUriSimple("xmpp:@");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlySlashFails() {
        new XmppUriSimple("xmpp:/");
    }

}
