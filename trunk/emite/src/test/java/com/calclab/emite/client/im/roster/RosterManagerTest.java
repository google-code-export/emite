package com.calclab.emite.client.im.roster;

import static com.calclab.emite.testing.TestMatchers.isListOfSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteStub;

public class RosterManagerTest {
    private EmiteStub emite;
    private RosterManager manager;
    private Roster roster;

    @Before
    public void aaCreate() {
	emite = new EmiteStub();
	roster = mock(Roster.class);
	manager = new RosterManager(emite, roster);
	manager.install();
    }

    @Test
    public void shouldRemoveItemsToRoster() {
	final XmppURI uri = XmppURI.parse("name@domain/res");
	manager.requestRemoveItem(uri);
	emite.verifySendCallback(new IQ(Type.set));
	emite.answerSuccess();
	verify(roster).removeItem(uri);
    }

    @Test
    public void shouldRequestRosterOnLogin() {
	emite.simulate(SessionManager.Events.loggedIn("user@domain/res"));
	emite.verifySendCallback(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null));
	emite.answer("<iq type='result' xmlns='jabber:client'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name1@domain' subscription='both' name='complete name1' />"
		+ "<item jid='name2@domain' subscription='both' name='complete name2' />" + "</query></iq>");
	verify(roster).setItems(isListOfSize(2));
    }
}
