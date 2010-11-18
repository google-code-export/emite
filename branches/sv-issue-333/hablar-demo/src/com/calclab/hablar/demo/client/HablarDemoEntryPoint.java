package com.calclab.hablar.demo.client;

import com.calclab.hablar.HablarComplete;
import com.calclab.hablar.HablarConfig;
import com.calclab.hablar.core.client.HablarDisplay;
import com.calclab.hablar.core.client.HablarWidget;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class HablarDemoEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	final DemoDisplay demo = new DemoWidget();
	new DemoPresenter(demo);
	final HablarWidget hablar = demo.getHablarWidget();

	// Default configuration
	final HablarConfig config = new HablarConfig();
	config.layout = HablarDisplay.Layout.accordion;
	config.dockRoster = "false";
	// Apply the configuration
	HablarComplete.install(hablar, config);

	// Add the widget to the html page
	RootLayoutPanel.get().add(demo.asWidget());
    }

}
