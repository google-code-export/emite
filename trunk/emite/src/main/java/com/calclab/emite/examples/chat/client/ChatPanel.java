/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.examples.chat.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatPanel extends DockPanel {

    public static interface ChatPanelListener {
	void onSend(String text);
    }

    private final TextBox input;
    private final VerticalPanel messages;

    public ChatPanel(final ChatPanelListener listener) {
	addStyleName("chatexample-chat");
	messages = new VerticalPanel();
	final ScrollPanel scroll = new ScrollPanel(messages);
	add(scroll, DockPanel.CENTER);
	final DockPanel send = new DockPanel();
	send.add(new Label("message: "), DockPanel.WEST);
	input = new TextBox();
	input.addKeyboardListener(new KeyboardListener() {
	    public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
	    }

	    public void onKeyPress(final Widget sender, final char keyCode, final int modifiers) {
		GWT.log("KEY code: " + keyCode, null);
	    }

	    public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
	    }

	});
	send.add(input, DockPanel.CENTER);
	final Button btnSend = new Button("send", new ClickListener() {
	    public void onClick(Widget sender) {
		send(listener);
	    }
	});
	send.add(btnSend, DockPanel.EAST);
	input.setFocus(true);
	add(send, DockPanel.SOUTH);
    }

    public void showIncomingMessage(final XmppURI fromURI, final String body) {
	messages.add(new Label(fromURI.toString() + ": " + body));
    }

    public void showOutcomingMessage(final String body) {
	messages.add(new Label("me: " + body));
    }

    private void send(final ChatPanelListener listener) {
	final String text = input.getText();
	if (text.length() > 0) {
	    listener.onSend(text);
	    input.setText("");
	}
	input.setFocus(true);
    }
}
