package com.calclab.emite.xep.avatar.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.avatar.client.AvatarVCard;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.testing.listener.MockListener;

public class AvatarManagerTest {
    private AvatarManager avatarManager;
    private MockedSession session;

    @Before
    public void aaaCreateManager() {
	session = new MockedSession();
	avatarManager = new AvatarManager(session);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void managerShouldListenPresenceWithPhoto() {
	final Listener<Presence> listener = Mockito.mock(Listener.class);
	avatarManager.onHashPresenceReceived(listener);
	final Presence presence = new Presence(XmppURI.uri(("juliet@capulet.com/balcony")));
	presence.addChild("x", "vcard-temp:x:update").addChild("photo", null).setText("sha1-hash-of-image");
	session.receives(presence);
	Mockito.verify(listener).onEvent(presence);
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
	final MockListener<AvatarVCard> listener = new MockListener<AvatarVCard>();
	avatarManager.onVCardReceived(listener);

	session.setLoggedIn(uri("romeo@montague.net/orchard"));
	avatarManager.requestVCard(XmppURI.uri("juliet@capulet.com"));
	session.verifyIQSent("<iq from='romeo@montague.net/orchard' to='juliet@capulet.com' type='get'>"
		+ "<vCard xmlns='vcard-temp'/></iq>");
	session.answer("<iq from='juliet@capulet.com' to='romeo@montague.net/orchard' type='result'>"
		+ "<vCard xmlns='vcard-temp'><PHOTO><TYPE>image/jpeg</TYPE>"
		+ "<BINVAL>Base64-encoded-avatar-file-here!</BINVAL></PHOTO></vCard></iq>");
	MockListener.verifyCalled(listener);
    }
}
