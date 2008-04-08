package com.calclab.examplechat.client.chatuiplugin.roster;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.MockitoXmpp;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class RosterUIPresenterTest {

    private String sessionUserJid;
    private PairChatUser otherUser;
    private Roster roster;
    private PresenceManager presenceManager;
    private PresenceListener presenceListener;
    private RosterUI rosterUI;
    private RosterUIView rosterUIView;
    private RosterManager rosterManager;

    @Before
    public void begin() {
        ChatDialogFactory factory = Mockito.mock(ChatDialogFactory.class);

        final XmppURI otherUri = XmppURI.parse("matt@example.com");
        final RosterItem rosterItem = new RosterItem(otherUri, Subscription.both, "matt");

        sessionUserJid = "lutherb@example.com";
        otherUser = new PairChatUser("", rosterItem, MultiChatView.DEF_PAIR_USER_COLOR);

        // Mocks creation
        MockitoXmpp xmpp = new MockitoXmpp();
        roster = xmpp.getRoster();
        rosterManager = xmpp.getRosterManager();
        presenceManager = xmpp.getPresenceManager();
        presenceListener = Mockito.mock(PresenceListener.class);
        presenceManager.addListener(presenceListener);
        rosterUI = Mockito.mock(RosterUI.class);
        rosterUIView = Mockito.mock(RosterUIView.class);
        final I18nTranslationServiceMocked i18n = new I18nTranslationServiceMocked();

        // Stubs
        Mockito.stub(factory.createrRosterUI(xmpp, i18n)).toReturn(rosterUI);
        Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
        Mockito.stub(rosterUI.getUserByJid(otherUri.getJID())).toReturn(otherUser);
    }

    @Test
    public void crearEmptyRoster() {
        rosterUI.clearRoster();
    }

    @Test
    public void someTest() {
        rosterManager.requestAddItem(otherUser.getJid(), otherUser.getAlias(), "FIXME");
        Presence presence = new Presence(Presence.Type.available, otherUser.getJid(), sessionUserJid);
        rosterManager.onPresenceReceived(presence);
        // RosterItem item = roster.findItemByURI(otherUser.getUri());
        // assertTrue(item != null);
        // Mockito.verify(rosterUIView,
        // Mockito.atLeastOnce()).addRosterItem((PairChatUser)
        // Mockito.anyObject(),
        // (UserGridMenuItemList) Mockito.anyObject());
    }
}
