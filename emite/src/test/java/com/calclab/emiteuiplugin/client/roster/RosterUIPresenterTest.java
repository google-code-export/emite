package com.calclab.emiteuiplugin.client.roster;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emiteui.client.MockitoXmpp;

public class RosterUIPresenterTest {

    private PresenceManager presenceManager;
    private PresenceListener presenceListener;
    private RosterUIPresenter rosterUI;
    private RosterUIView rosterUIView;

    private RosterItem rosterItem;
    private XmppURI otherUri;
    private XmppURI meUri;

    @Test
    public void availableTypeMustShowApropiateIcon() {
        String statusText = "Some status text";
        assertEquals(UserStatusIcon.available, getPresenceIcon(Type.available, Show.notSpecified, null));
        assertEquals(UserStatusIcon.away, getPresenceIcon(Type.unavailable, Show.away, null));
        assertEquals(UserStatusIcon.chat, getPresenceIcon(Type.available, Show.chat, statusText));
        assertEquals(UserStatusIcon.xa, getPresenceIcon(Type.available, Show.xa, statusText));
        assertEquals(UserStatusIcon.away, getPresenceIcon(Type.available, Show.away, statusText));
        assertEquals(UserStatusIcon.offline, getPresenceIcon(Type.unavailable, Show.notSpecified, statusText));
        assertEquals(UserStatusIcon.unknown, getPresenceIcon(Type.unavailable, Show.unknown, null));
    }

    @Before
    public void begin() {
        meUri = uri("me@example.com");
        otherUri = uri("matt@example.com");
        rosterItem = new RosterItem(otherUri, Subscription.both, "matt");

        // Mocks creation
        final MockitoXmpp xmpp = new MockitoXmpp();
        presenceManager = xmpp.getPresenceManager();
        presenceListener = Mockito.mock(PresenceListener.class);
        presenceManager.addListener(presenceListener);
        rosterUIView = Mockito.mock(RosterUIView.class);
        final I18nTranslationServiceMocked i18n = new I18nTranslationServiceMocked();

        rosterUI = new RosterUIPresenter(xmpp, i18n);
        rosterUI.init(rosterUIView);

        // Stubs
        Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
    }

    @Test
    public void crearEmptyRoster() {
        rosterUI.clearRoster();
    }

    @Test
    public void nullStatusTextMustReturnSpace() {
        // space? yes, a gwt-ext issue
        assertEquals(" ", rosterUI.formatRosterItemStatusText(null, null));
        Presence presence = createPresence(Type.available, Show.dnd, null);
        assertEquals(" ", rosterUI.formatRosterItemStatusText(presence, null));
        presence = createPresence(Type.available, Show.dnd, "null");
        assertEquals(" ", rosterUI.formatRosterItemStatusText(presence, null));
    }

    @Test
    public void subscribeAction() {
        rosterUI.doAction(RosterUIPresenter.ON_REQUEST_SUBSCRIBE, rosterItem.getJID());
        Mockito.verify(presenceManager).requestSubscribe(otherUri);
    }

    @Test
    public void unSubscribeAction() {
        rosterUI.doAction(RosterUIPresenter.ON_CANCEL_SUBSCRITOR, rosterItem.getJID());
        Mockito.verify(presenceManager).cancelSubscriptor(otherUri);
    }

    private Presence createPresence(final Type type, final Show show, final String status) {
        Presence presence = new Presence(type, otherUri, meUri);
        presence.setShow(show);
        presence.setStatus(status);
        return presence;
    }

    private UserStatusIcon getPresenceIcon(final Type type, final Show show, final String status) {
        Presence presence = new Presence(type, otherUri, meUri);
        presence.setShow(show);
        presence.setStatus(status);
        return rosterUI.getPresenceIcon(presence);
    }
}
