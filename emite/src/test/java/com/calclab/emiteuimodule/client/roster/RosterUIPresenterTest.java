package com.calclab.emiteuimodule.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.i18n.I18nTranslationServiceMocked;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.SubscriptionState;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;
import com.calclab.emiteuimodule.client.utils.ChatIconDescriptor;

public class RosterUIPresenterTest {

    private RosterUIPresenter rosterUI;
    private RosterItem rosterItem;
    private XmppURI otherUri;
    private I18nTranslationServiceMocked i18n;
    private RosterUIView rosterUIView;

    @Test
    public void availablePresenceMustReturnOnline() {
	RosterItem item = createRosterItem(SubscriptionState.both, Show.notSpecified, true);
	assertEquals("Online", rosterUI.formatRosterItemStatusText(item));
    }

    @Test
    public void availableTypeMustShowApropiateIcon() {
	final String statusText = "Some status text";
	assertEquals(ChatIconDescriptor.available, getPresenceIcon(Show.notSpecified, null, true));
	assertEquals(ChatIconDescriptor.offline, getPresenceIcon(null, null, false));
	assertEquals(ChatIconDescriptor.chat, getPresenceIcon(Show.chat, statusText, true));
	assertEquals(ChatIconDescriptor.xa, getPresenceIcon(Show.xa, statusText, true));
	assertEquals(ChatIconDescriptor.away, getPresenceIcon(Show.away, statusText, true));
	assertEquals(ChatIconDescriptor.unknown, getPresenceIcon(Show.unknown, statusText, true));
	// assertEquals(ChatIconDescriptor.unknown,
	// getPresenceIcon(Type.unavailable, Show.unknown, null));
    }

    @Before
    public void begin() {
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
	rosterItem.setAvailable(true);
	final ChatUserUI user = new ChatUserUI("", rosterItem, "black");
	user.setVisible(false);
	rosterUI.refreshRosterItemInView(rosterItem, user, false);
	Mockito.verify(rosterUIView).addRosterItem(Mockito.eq(user), (UserGridMenuItemList) Mockito.anyObject());
    }

    @Test
    public void nullStatusTextMustReturnSpace() {
	// space? yes, a gwt-ext issue
	assertEquals(rosterUI.getDefaultStatusText(true, Show.dnd), rosterUI
		.formatRosterItemStatusText(createRosterItem(SubscriptionState.both, Show.dnd, null, true)));
	assertEquals(rosterUI.getDefaultStatusText(true, Show.dnd), rosterUI
		.formatRosterItemStatusText(createRosterItem(SubscriptionState.both, Show.dnd, "null", true)));
    }

    @Test
    public void unavailablePresenceMustReturnOffline() {
	assertEquals("Offline", rosterUI.formatRosterItemStatusText(createRosterItem(SubscriptionState.both,
		Show.unknown, false)));
    }

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
	rosterItem.setAvailable(true);
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

    private RosterItem createRosterItem(final SubscriptionState subs, final boolean available) {
	RosterItem item = new RosterItem(otherUri, subs, "other", null);
	item.setAvailable(available);
	return item;
    }

    private RosterItem createRosterItem(final SubscriptionState subs, final Show show, final boolean available) {
	RosterItem item = createRosterItem(subs, available);
	item.setShow(show);
	return item;
    }

    private RosterItem createRosterItem(final SubscriptionState subs, final Show show, final String status,
	    final boolean available) {
	RosterItem item = createRosterItem(subs, show, available);
	item.setStatus(status);
	return item;
    }

    private ChatIconDescriptor getPresenceIcon(final Show show, final String status, final boolean available) {
	RosterItem item = new RosterItem(otherUri, SubscriptionState.both, "someone", null);
	item.setShow(show);
	item.setStatus(status);
	item.setAvailable(available);
	return rosterUI.getPresenceIcon(item);
    }
}
