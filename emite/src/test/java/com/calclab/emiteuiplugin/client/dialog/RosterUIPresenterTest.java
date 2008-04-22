package com.calclab.emiteuiplugin.client.dialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteui.client.MockitoXmpp;
import com.calclab.emiteuiplugin.client.roster.RosterUIPresenter;
import com.calclab.emiteuiplugin.client.roster.RosterUIView;

public class RosterUIPresenterTest {

    private PresenceManager presenceManager;
    private PresenceListener presenceListener;
    private RosterUIPresenter rosterUI;
    private RosterUIView rosterUIView;

    private RosterItem rosterItem;
    private XmppURI otherUri;

    @Before
    public void begin() {
        otherUri = XmppURI.parse("matt@example.com");
        rosterItem = new RosterItem(otherUri, Subscription.both, "matt");

        // Mocks creation
        MockitoXmpp xmpp = new MockitoXmpp();
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
    public void subscribeAction() {
        rosterUI.doAction(RosterUIPresenter.ON_REQUEST_SUBSCRIBE, rosterItem.getJID());
        Mockito.verify(presenceManager).requestSubscribe(otherUri);
    }

    @Test
    public void unSubscribeAction() {
        rosterUI.doAction(RosterUIPresenter.ON_CANCEL_SUBSCRITOR, rosterItem.getJID());
        Mockito.verify(presenceManager).cancelSubscriptor(otherUri);

    }
}
