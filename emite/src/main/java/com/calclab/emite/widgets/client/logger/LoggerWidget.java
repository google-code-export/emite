/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client.logger;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.events.Event0;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoggerWidget extends DockPanel implements EmiteWidget {
    public static final String ERROR = "error";
    public static final String SENT = "sent";
    public static final String RECEIVED = "received";
    private final VerticalPanel content;
    Event0 onClear;
    private final ScrollPanel scroll;

    public LoggerWidget() {
	this.onClear = new Event0("widgets.logger:onClear");
	content = new VerticalPanel();
	final Button clear = new Button("clear", new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		onClear.fire();
	    }
	});
	final FlowPanel panel = new FlowPanel();
	panel.add(clear);
	add(panel, DockPanel.SOUTH);

	scroll = new ScrollPanel();
	scroll.addStyleName("content");
	scroll.setAlwaysShowScrollBars(true);
	scroll.add(content);
	add(scroll, DockPanel.CENTER);
    }

    public void clearContent() {
	content.clear();
    }

    public String[] getParamNames() {
	return new String[] {};
    }

    public void setParam(final String name, final String value) {
    }

    public void write(final String color, final String message) {
	final Label label = new Label(message);
	label.addStyleName(color);
	content.add(label);
	scroll.ensureVisible(label);
    }

}
