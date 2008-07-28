package com.calclab.emite.client.xep.avatar;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.testing.MockSlot;
import com.calclab.suco.testing.signal.SignalTester;

public class AvatarManagerTest {
    private AvatarManager avatarManager;
    private PresenceManager presenceManager;
    private MockedSession session;

    @Before
    public void aaaCreateManager() {
	session = new MockedSession();
	presenceManager = Mockito.mock(PresenceManager.class);
	avatarManager = new AvatarManager(session, presenceManager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void managerShouldListenPresenceWithPhoto() {
	final Slot<Presence> slot = Mockito.mock(Slot.class);
	avatarManager.onHashPresenceReceived(slot);
	final Presence presence = new Presence(XmppURI.uri(("juliet@capulet.com/balcony")));
	presence.addChild("x", "vcard-temp:x:update").addChild("photo", null).setText("sha1-hash-of-image");
	final SignalTester<Presence> signalTester = new SignalTester<Presence>();
	Mockito.verify(presenceManager).onPresenceReceived(Mockito.argThat(signalTester));
	signalTester.fire(presence);
	Mockito.verify(slot).onEvent(presence);
    }

    @Test
    public void managerShouldPublishAvatar() {
	session.setLoggedIn(uri("romeo@montague.net/orchard"));
	final String photo = "some base64 encoded photo";
	avatarManager.setVCardAvatar(photo);
	session.verifyIQSent("<iq from='romeo@montague.net/orchard' type='set'>"
		+ "<vCard prodid='-//HandGen//NONSGML vGen v1.0//EN' version='2.0' "
		+ "xmlns='vcard-temp' xdbns='vcard-temp'>"
		+ "<PHOTO><BINVAL>some base64 encoded photo</BINVAL></PHOTO></vCard></iq>");
	session.answerSuccess();
	// User's Server Acknowledges Publish:
	// <iq to='juliet@capulet.com' type='result' id='vc1'/>
    }

    @Test
    public void verifySendVcardRequest() {
	final MockSlot<AvatarVCard> slot = new MockSlot<AvatarVCard>();
	avatarManager.onVCardReceived(slot);

	session.setLoggedIn(uri("romeo@montague.net/orchard"));
	avatarManager.requestVCard(XmppURI.uri("juliet@capulet.com"));
	session.verifyIQSent("<iq from='romeo@montague.net/orchard' to='juliet@capulet.com' type='get'>"
		+ "<vCard xmlns='vcard-temp'/></iq>");
	session.answer("<iq from='juliet@capulet.com' to='romeo@montague.net/orchard' type='result'>"
		+ "<vCard xmlns='vcard-temp'><PHOTO><TYPE>image/jpeg</TYPE>"
		+ "<BINVAL>Base64-encoded-avatar-file-here!</BINVAL></PHOTO></vCard></iq>");
	MockSlot.verifyCalled(slot);
    }
}
