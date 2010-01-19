package com.calclab.hablar.basic.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionHandler.Behaviour;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.hablar.basic.client.ui.HablarResources;
import com.calclab.hablar.basic.client.ui.icons.DefaultHablarIcons;
import com.calclab.suco.client.Suco;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class HablarBasicEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	DefaultHablarIcons.init();

	final SubscriptionHandler handler = Suco.get(SubscriptionHandler.class);
	handler.setBehaviour(Behaviour.acceptAll);

	final HablarResources res = GWT.create(HablarResources.class);
	res.generalCSS().ensureInjected();

	// FIXME: revise this (needed to Mock i18n for tests)
	Suco.install(new HablarModule());

	final XmppURI host = XmppURI.uri(PageAssist.getMeta("emite.searchHost"));
	Suco.get(SearchManager.class).setHost(host);

    }

}