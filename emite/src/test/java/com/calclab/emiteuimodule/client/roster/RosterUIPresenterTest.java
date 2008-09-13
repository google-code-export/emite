package com.calclab.emiteuimodule.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.xold_roster.XRosterItem;
import com.calclab.emite.im.client.xold_roster.XRosterItem.Subscription;
import com.calclab.emiteuimodule.client.MockitoXmpp;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;

public class RosterUIPresenterTest {

    private RosterUIPresenter rosterUI;
    private XRosterItem xRosterItem;
    private XmppURI otherUri;
    private XmppURI meUri;
    private I18nTranslationServiceMocked i18n;
    private RosterUIView rosterUIView;

    @Test
    public void availablePresenceMustReturnOnline() {
	final Presence presence = createPresence(Type.available, null, null);
	assertEquals("Online", rosterUI.formatRosterItemStatusText(presence, null));
    }

    @Test
    public void availableTypeMustShowApropiateIcon() {
	final String statusText = "Some status text";
	assertEquals(ChatIconDescriptor.available, getPresenceIcon(Type.available, Show.notSpecified, null));
	assertEquals(ChatIconDescriptor.offline, getPresenceIcon(Type.unavailable, Show.away, null));
	assertEquals(ChatIconDescriptor.chat, getPresenceIcon(Type.available, Show.chat, statusText));
	assertEquals(ChatIconDescriptor.xa, getPresenceIcon(Type.available, Show.xa, statusText));
	assertEquals(ChatIconDescriptor.away, getPresenceIcon(Type.available, Show.away, statusText));
	assertEquals(ChatIconDescriptor.offline, getPresenceIcon(Type.unavailable, Show.notSpecified, statusText));
	assertEquals(ChatIconDescriptor.unknown, getPresenceIcon(Type.unavailable, Show.unknown, null));
    }

    @Before
    public void begin() {
	meUri = uri("me@example.com");
	otherUri = uri("matt@example.com");
	xRosterItem = new XRosterItem(otherUri, Subscription.both, "matt");

	// Mocks creation
	final MockitoXmpp xmpp = new MockitoXmpp();
	rosterUIView = Mockito.mock(RosterUIView.class);
	i18n = new I18nTranslationServiceMocked();
	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};

	rosterUI = new RosterUIPresenter(xmpp, i18n, avatarProvider);
	rosterUI.init(rosterUIView);

	// Stubs
	Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
    }

    @Test
    public void crearEmptyRoster() {
	rosterUI.clearRoster();
    }

    @Test
    public void novisibleItemAvailableMustAdd() {
	xRosterItem.setPresence(createPresence(Type.available, Show.notSpecified, "Nothing"));
	final ChatUserUI user = new ChatUserUI("", xRosterItem, "black");
	user.setVisible(false);
	rosterUI.refreshRosterItemInView(xRosterItem, user, true);
	Mockito.verify(rosterUIView).addRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void novisibleItemAvailableMustAddwithNotShowUnavailable() {
	xRosterItem.setPresence(createPresence(Type.available, Show.notSpecified, "Nothing"));
	final ChatUserUI user = new ChatUserUI("", xRosterItem, "black");
	user.setVisible(false);
	rosterUI.refreshRosterItemInView(xRosterItem, user, false);
	Mockito.verify(rosterUIView).addRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void nullStatusTextMustReturnSpace() {
	// space? yes, a gwt-ext issue
	assertEquals(" ", rosterUI.formatRosterItemStatusText(null, null));
	Presence presence = createPresence(Type.available, Show.dnd, null);
	assertEquals(rosterUI.getShowText(Type.available, Show.dnd), rosterUI
		.formatRosterItemStatusText(presence, null));
	presence = createPresence(Type.available, Show.dnd, "null");
	assertEquals(rosterUI.getShowText(Type.available, Show.dnd), rosterUI
		.formatRosterItemStatusText(presence, null));
    }

    @Test
    public void unavailablePresenceMustReturnOffline() {
	final Presence presence = createPresence(Type.unavailable, null, null);
	assertEquals("Offline", rosterUI.formatRosterItemStatusText(presence, null));
    }

    @Test
    public void visibleItemAvailableMustUpdate() {
	xRosterItem.setPresence(createPresence(Type.available, Show.notSpecified, "Nothing"));
	final ChatUserUI user = new ChatUserUI("", xRosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(xRosterItem, user, true);
	Mockito.verify(rosterUIView).updateRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void visibleItemAvailableMustUpdateWithNotShowUnavailable() {
	xRosterItem.setPresence(createPresence(Type.available, Show.notSpecified, "Nothing"));
	final ChatUserUI user = new ChatUserUI("", xRosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(xRosterItem, user, false);
	Mockito.verify(rosterUIView).updateRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void visibleItemUnavailableMustRemoveWithNotShowAvailable() {
	xRosterItem.setPresence(createPresence(Type.unavailable, Show.notSpecified, "Nothing"));
	final ChatUserUI user = new ChatUserUI("", xRosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(xRosterItem, user, false);
	Mockito.verify(rosterUIView).removeRosterItem(user);
    }

    @Test
    public void visibleItemUnavailableMustUpdate() {
	xRosterItem.setPresence(createPresence(Type.unavailable, Show.notSpecified, "Nothing"));
	final ChatUserUI user = new ChatUserUI("", xRosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(xRosterItem, user, true);
	Mockito.verify(rosterUIView).updateRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    private Presence createPresence(final Type type, final Show show, final String status) {
	final Presence presence = new Presence(type, otherUri, meUri);
	presence.setShow(show);
	presence.setStatus(status);
	return presence;
    }

    private ChatIconDescriptor getPresenceIcon(final Type type, final Show show, final String status) {
	final Presence presence = new Presence(type, otherUri, meUri);
	presence.setShow(show);
	presence.setStatus(status);
	return rosterUI.getPresenceIcon(presence);
    }
}
