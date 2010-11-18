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
package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.base.GWTExtensibleWidget;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class GWTAbstractChatWidget extends GWTExtensibleWidget implements AbstractChatWidget {
    private final TextArea area;
    private final TextBox input;
    private final Button send;
    protected Event<String> onSendMessage;
    private AbstractChatController controller;

    public GWTAbstractChatWidget() {
	this.onSendMessage = new Event<String>("widgets:room:sendMessage");
	this.area = new TextArea();
	this.input = new TextBox();

	input.addKeyPressHandler(new KeyPressHandler() {
	    public void onKeyPress(final KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == 13) {
		    sendMessage();
		}
	    }
	});

	this.send = new Button("send", new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		sendMessage();
	    }
	});
	final HorizontalPanel inputBar = new HorizontalPanel();
	inputBar.add(input);
	inputBar.add(send);

	add(area, DockPanel.CENTER);
	add(inputBar, DockPanel.SOUTH);
    }

    public void addWidget(final DockLayoutConstant layoutConstant, final EmiteWidget widget) {
	assert layoutConstant != CENTER;

	add((Widget) widget, layoutConstant);
    }

    public AbstractChatController getController() {
	return controller;
    }

    public void onSendMessage(final Listener<String> listener) {
	onSendMessage.add(listener);
    }

    public void setChat(final Chat chat) {
	controller.setChat(chat);
    }

    public void setController(final AbstractChatController controller) {
	this.controller = controller;
    }

    public void setInputEnabled(final boolean enabled) {
	area.setEnabled(enabled);
	input.setEnabled(enabled);
	send.setEnabled(enabled);
    }

    public void write(final String from, final String message) {
	final String text = area.getText();
	final String prefix = from != null ? from + ": " : "";
	area.setText(text + prefix + message + "\n");
    }

    private void sendMessage() {
	final String text = input.getText();
	if (text.length() > 0) {
	    onSendMessage.fire(text);
	    input.setText("");
	    input.setFocus(true);
	}
    }

}
