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
package com.calclab.emite.widgets.habla.client;

import java.util.Map;

import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

public class HablaWidget extends AbsolutePanel {
    private final Event<Map<String, String>> onSetProperties;
    private final Event<String> onMessage;
    private final HTML output;
    private final TextArea input;
    private final Label status;

    public HablaWidget() {
	this.onSetProperties = new Event<Map<String, String>>("comenta:onSetProperty");
	this.onMessage = new Event<String>("comenta:onMessage");

	this.status = new Label();
	this.output = new HTML();
	this.input = new TextArea();
	input.addKeyPressHandler(new KeyPressHandler() {
	    public void onKeyPress(final KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == 13) {
		    onMessage.fire(input.getText());
		    input.setText("");
		    input.setFocus(true);
		}
	    }
	});

	initLayout();
    }

    public String[] getPropertyNames() {
	return new String[] { "jid" };
    }

    public void onMessage(final Listener<String> listener) {
	onMessage.add(listener);
    }

    public void onSetProperties(final Listener<Map<String, String>> listener) {
	onSetProperties.add(listener);
    }

    public void setEnabled(final boolean enabled) {
	input.setEnabled(enabled);
    }

    public void setProperties(final Map<String, String> properties) {
	onSetProperties.fire(properties);
    }

    public void show(final String name, final String body) {
	final String userClass = "user";
	final String user = name != null ? "<span class=\"" + userClass + "\">" + name + "</span>: " : "";
	final String line = "<div>" + user + body + "</div>";
	output.setHTML(output.getHTML() + line);
    }

    public void showStatus(final String message, final String cssClass) {
	status.setText(message);
    }

    private void initLayout() {
	final int width = 300;
	final int height = 300;
	this.setPixelSize(width, height);
	this.setStylePrimaryName("emite-widgets-Comenta");

	final Label top = new Label();
	add(top, 0, 0);
	top.setPixelSize(width, 30);
	top.setStylePrimaryName("top");
	add(status, 10, 10);
	status.setPixelSize(width, 35);

	final Label middle = new Label();
	add(middle, 0, 31);
	middle.setPixelSize(width, height - 85);
	middle.setStylePrimaryName("middle");
	add(output, 0, 32);
	output.setPixelSize(100, 100);

	final Label bottom = new Label();
	add(bottom, 0, height - 51);
	bottom.setPixelSize(width, 51);
	bottom.setStylePrimaryName("bottom");
	add(input, 10, height - 43);
	input.setPixelSize(width - 20, 30);
    }

}
