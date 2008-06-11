package com.calclab.emite.client.xep.avatar;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.SignalTester;
import com.calclab.suco.client.signal.Slot;

public class AvatarManagerTest {
    private EmiteTestHelper emite;
    private AvatarManager avatarManager;
    private PresenceManager presenceManager;

    @Before
    public void aaaCreateManager() {
	emite = new EmiteTestHelper();
	presenceManager = Mockito.mock(PresenceManager.class);
	avatarManager = new AvatarManager(emite, presenceManager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void managerShouldListenPresenceWithPhoto() {
	Slot<Presence> slot = Mockito.mock(Slot.class);
	avatarManager.avatarHashPresenceReceived(slot);
	Presence presence = new Presence(XmppURI.uri(("juliet@capulet.com/balcony")));
	presence.addChild("x", "vcard-temp:x:update").addChild("photo", null).setText("sha1-hash-of-image");
	SignalTester<Presence> signalTester = new SignalTester<Presence>();
	Mockito.verify(presenceManager).onPresenceReceived(Mockito.argThat(signalTester));
	signalTester.fire(presence);
	Mockito.verify(slot).onEvent(presence);
    }

    @Test
    public void managerShouldPublishAvatar() {
	avatarManager.logIn(uri("romeo@montague.net/orchard"));
	final String photo = "some base64 encoded photo";
	avatarManager.setVCardAvatar(photo);
	emite.verifyIQSent("<iq from='romeo@montague.net/orchard' type='set'>"
		+ "<vCard prodid='-//HandGen//NONSGML vGen v1.0//EN' version='2.0' "
		+ "xmlns='vcard-temp' xdbns='vcard-temp'>"
		+ "<PHOTO><BINVAL>some base64 encoded photo</BINVAL></PHOTO></vCard></iq>");
	emite.answerSuccess();
	// User's Server Acknowledges Publish:
	// <iq to='juliet@capulet.com' type='result' id='vc1'/>
    }

    @SuppressWarnings("unchecked")
    @Test
    public void verifyReceiveVCardPhoto() {
	avatarManager.logIn(uri("romeo@montague.net/orchard"));
	Slot<AvatarVCard> slot = Mockito.mock(Slot.class);
	avatarManager.onAvatarVCardReceived(slot);
	emite.receives("<iq from='juliet@capulet.com' to='romeo@montague.net/orchard' type='result'"
		+ "id='vc2'><vCard xmlns='vcard-temp'>"
		+ "<PHOTO><TYPE>image/jpeg</TYPE><BINVAL>Base64-encoded-avatar-file-here!</BINVAL>"
		+ "</PHOTO></vCard></iq>");
	Mockito.verify(slot).onEvent((AvatarVCard) Mockito.anyObject());
    }

    @Test
    public void verifySendVcardRequest() {
	avatarManager.logIn(uri("romeo@montague.net/orchard"));
	avatarManager.requestVCard(XmppURI.uri("juliet@capulet.com"));
	emite.verifySent("<iq from='romeo@montague.net/orchard' to='juliet@capulet.com' type='get' id='vc2'>"
		+ "<vCard xmlns='vcard-temp'/></iq>");
    }
}
