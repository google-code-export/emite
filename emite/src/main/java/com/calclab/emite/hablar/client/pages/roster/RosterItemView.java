package com.calclab.emite.hablar.client.pages.roster;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.roster.RosterItem;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class RosterItemView extends FlowPanel {
    public static interface RosterItemViewListener {
	void onAction(RosterItemView view);
    }

    private RosterItem item;
    private final HTML show;
    private final Hyperlink jid;
    private final Hyperlink menu;

    public RosterItemView(final RosterItem item, final RosterItemViewListener listener) {
	setStyleName("hablar-RosterItemView");
	this.show = new HTML();
	this.jid = new Hyperlink();
	this.menu = new Hyperlink();
	menu.setText("menu");
	add(show);
	add(jid);
	add(menu);

	jid.addClickListener(new ClickListener() {
	    public void onClick(final Widget arg0) {
		listener.onAction(RosterItemView.this);
	    }
	});

	setItem(item);
    }

    public RosterItem getItem() {
	return item;
    }

    public void setItem(final RosterItem item) {
	this.item = item;
	final Show s = item.getPresence().getShow();
	show.setHTML("<b>" + s + "</b>");
	jid.setText(item.getJID().toString());
    }

}
