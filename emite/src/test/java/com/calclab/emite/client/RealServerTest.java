package com.calclab.emite.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RealServerTest {

    @Test
    public synchronized void testSimple() throws InterruptedException {
        final AbstractXmpp xmpp = TestHelper.createXMPP();
        final Session session = xmpp.getSession();

        assertEquals(Session.State.disconnected, session.getState());
        xmpp.getSession().addListener(new SessionListener() {
            public void onStateChanged(final State old, final State current) {
                Log.debug("Session state: " + current);
                switch (current) {
                case connected:
                    Chat chat = xmpp.getChat().newChat(XmppURI.parse("testuser1@localhost"));
                    chat.send("hola!");
                }
            }
        });
        xmpp.login("admin", "easyeasy");
        wait(6000);
        assertEquals(Session.State.connected, session.getState());
    }
}
