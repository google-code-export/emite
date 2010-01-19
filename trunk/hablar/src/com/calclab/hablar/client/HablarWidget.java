package com.calclab.hablar.client;

import com.calclab.hablar.client.HablarConfig.Layout;
import com.calclab.hablar.client.chat.ChatConfig;
import com.calclab.hablar.client.chat.ChatManagerLogic;
import com.calclab.hablar.client.ui.HablarPanel;
import com.calclab.hablar.client.ui.pages.PagesWidget;
import com.calclab.hablar.client.ui.pages.panel.AccordionPages;
import com.calclab.hablar.client.ui.pages.panel.TabPages;
import com.google.gwt.user.client.ui.Composite;

public class HablarWidget extends Composite {

    public HablarWidget(HablarConfig config) {
	PagesWidget pages = null;
	if (config.layout == Layout.accordion) {
	    pages = new PagesWidget(new AccordionPages());
	} else if (config.layout == Layout.tabs) {
	    pages = new PagesWidget(new TabPages());
	} else {
	    throw new RuntimeException("Layout not configured.");
	}
	HablarPanel panel = new HablarPanel(config, pages);
	new HablarLogic(config, panel, pages);
	new ChatManagerLogic(ChatConfig.getFromMeta(), pages);
	initWidget(panel);
    }

}