package com.calclab.hablar.demo.client;

import com.calclab.hablar.basic.client.ui.HablarWidget;
import com.calclab.hablar.chat.client.HablarChat;
import com.calclab.hablar.roster.client.HablarRoster;
import com.calclab.hablar.search.client.HablarSearch;
import com.calclab.hablar.signals.client.HablarSignals;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class HablarDemoEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	DemoView demo = new DemoWidget();
	HablarWidget hablar = demo.getHablar();

	HablarChat.install(hablar);
	HablarRoster.install(hablar, false);
	HablarSearch.install(hablar);
	HablarSignals.install(hablar);

	new DemoPresenter(demo);
	RootLayoutPanel.get().add(demo.asWidget());
    }

}
