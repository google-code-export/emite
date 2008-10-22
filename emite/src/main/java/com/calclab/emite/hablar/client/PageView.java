package com.calclab.emite.hablar.client;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class PageView extends DockPanel {

    private FlowPanel toolbar;
    private final DeckPanel content;

    public PageView() {
	setStyleName("hablar-Page");
	content = new DeckPanel();
	final FlowPanel flow = new FlowPanel();
	flow.add(content);
	add(flow, DockPanel.CENTER);
    }

    public void addContent(final Widget w) {
	content.add(w);
	content.showWidget(content.getWidgetCount() - 1);
    }

    public FlowPanel getToolbar() {
	if (toolbar == null) {
	    toolbar = new FlowPanel();
	    toolbar.setStyleName("toolbar");
	    add(toolbar, DockPanel.NORTH);
	}
	return toolbar;
    }

    public void setToolbarVisible(final boolean visible) {
	getToolbar().setVisible(visible);
    }

    public void showContent(final int index) {
	content.showWidget(index);
    }

}
