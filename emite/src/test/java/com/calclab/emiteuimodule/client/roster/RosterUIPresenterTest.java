package com.calclab.emiteuimodule.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.SubscriptionState;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;

public class RosterUIPresenterTest {

    private RosterUIPresenter rosterUI;
    private RosterItem rosterItem;
    private XmppURI otherUri;
    private XmppURI meUri;
    private I18nTranslationServiceMocked i18n;
    private RosterUIView rosterUIView;

    // FIXME
    // @Test
    // public void availablePresenceMustReturnOnline() {
    // final Presence presence = createPresence(Type.available, null, null);
    // assertEquals("Online", rosterUI.formatRosterItemStatusText(presence,
    // null));
    // }

    // @Test
    // public void availableTypeMustShowApropiateIcon() {
    // final String statusText = "Some status text";
    // assertEquals(ChatIconDescriptor.available,
    // getPresenceIcon(Type.available, Show.notSpecified, null));
    // assertEquals(ChatIconDescriptor.offline,
    // getPresenceIcon(Type.unavailable, Show.away, null));
    // assertEquals(ChatIconDescriptor.chat, getPresenceIcon(Type.available,
    // Show.chat, statusText));
    // assertEquals(ChatIconDescriptor.xa, getPresenceIcon(Type.available,
    // Show.xa, statusText));
    // assertEquals(ChatIconDescriptor.away, getPresenceIcon(Type.available,
    // Show.away, statusText));
    // assertEquals(ChatIconDescriptor.offline,
    // getPresenceIcon(Type.unavailable, Show.notSpecified, statusText));
    // assertEquals(ChatIconDescriptor.unknown,
    // getPresenceIcon(Type.unavailable, Show.unknown, null));
    // }

    @Before
    public void begin() {
	meUri = uri("me@example.com");
	otherUri = uri("matt@example.com");
	rosterItem = new RosterItem(otherUri, SubscriptionState.both, "matt", null);

	// Mocks creation
	rosterUIView = Mockito.mock(RosterUIView.class);
	final Roster roster = Mockito.mock(Roster.class);
	final SubscriptionManager rosterManager = Mockito.mock(SubscriptionManager.class);
	i18n = new I18nTranslationServiceMocked();
	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(final XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};

	rosterUI = new RosterUIPresenter(roster, rosterManager, i18n, avatarProvider);
	rosterUI.init(rosterUIView);

    }

    @Test
    public void crearEmptyRoster() {
	rosterUI.clearRoster();
    }

    @Test
    public void novisibleItemAvailableMustAdd() {
	// rosterItem.setPresence(createPresence(Type.available,
	// Show.notSpecified, "Nothing"));
	rosterItem.setShow(Show.notSpecified);
	rosterItem.setStatus("Nothing");
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(false);
	rosterUI.refreshRosterItemInView(rosterItem, user, true);
	Mockito.verify(rosterUIView).addRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void novisibleItemAvailableMustAddwithNotShowUnavailable() {
	// rosterItem.setPresence(createPresence(Type.available,
	// Show.notSpecified, "Nothing"));
	rosterItem.setShow(Show.notSpecified);
	rosterItem.setStatus("Nothing");
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(false);
	rosterUI.refreshRosterItemInView(rosterItem, user, false);
	Mockito.verify(rosterUIView).addRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    // FIXME
    // @Test
    // public void nullStatusTextMustReturnSpace() {
    // // space? yes, a gwt-ext issue
    // assertEquals(" ", rosterUI.formatRosterItemStatusText(null, null));
    // Presence presence = createPresence(Type.available, Show.dnd, null);
    // assertEquals(rosterUI.getShowText(Type.available, Show.dnd), rosterUI
    // .formatRosterItemStatusText(presence, null));
    // presence = createPresence(Type.available, Show.dnd, "null");
    // assertEquals(rosterUI.getShowText(Type.available, Show.dnd), rosterUI
    // .formatRosterItemStatusText(presence, null));
    // }

    // @Test
    // public void unavailablePresenceMustReturnOffline() {
    // final Presence presence = createPresence(Type.unavailable, null, null);
    // assertEquals("Offline", rosterUI.formatRosterItemStatusText(presence,
    // null));
    // }

    @Test
    public void visibleItemAvailableMustUpdate() {
	// rosterItem.setPresence(createPresence(Type.available,
	// Show.notSpecified, "Nothing"));
	rosterItem.setShow(Show.notSpecified);
	rosterItem.setStatus("Nothing");
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(rosterItem, user, true);
	Mockito.verify(rosterUIView).updateRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void visibleItemAvailableMustUpdateWithNotShowUnavailable() {
	// rosterItem.setPresence(createPresence(Type.available,
	// Show.notSpecified, "Nothing"));
	rosterItem.setShow(Show.notSpecified);
	rosterItem.setStatus("Nothing");
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(rosterItem, user, false);
	Mockito.verify(rosterUIView).updateRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void visibleItemUnavailableMustRemoveWithNotShowAvailable() {
	// rosterItem.setPresence(createPresence(Type.unavailable,
	// Show.notSpecified, "Nothing"));
	rosterItem.setShow(Show.unknown);
	rosterItem.setStatus("Nothing");
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(rosterItem, user, false);
	Mockito.verify(rosterUIView).removeRosterItem(user);
    }

    @Test
    public void visibleItemUnavailableMustUpdate() {
	// rosterItem.setPresence(createPresence(Type.unavailable,
	// Show.notSpecified, "Nothing"));
	rosterItem.setShow(Show.notSpecified);
	rosterItem.setStatus("Nothing");
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(true);
	rosterUI.refreshRosterItemInView(rosterItem, user, true);
	Mockito.verify(rosterUIView).updateRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    private Presence createPresence(final Type type, final Show show, final String status) {
	final Presence presence = new Presence(type, otherUri, meUri);
	presence.setShow(show);
	presence.setStatus(status);
	return presence;
    }

    // private ChatIconDescriptor getPresenceIcon(final Type type, final Show
    // show, final String status) {
    // final Presence presence = new Presence(type, otherUri, meUri);
    // presence.setShow(show);
    // presence.setStatus(status);
    // return rosterUI.getPresenceIcon(presence);
    // }
}
