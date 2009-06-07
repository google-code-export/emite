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
package com.calclab.emiteui.client.demo;

import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emiteui.client.DemoParameters;
import com.calclab.emiteui.client.demo.EmiteDemoLoginPanel.LoginPanelListener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;

public class EmiteDemoUI {

    public static interface EmiteDemoChatIconListener {
	void onClick();
    }

    private final DemoParameters params;

    public EmiteDemoUI(final DemoParameters params) {
	this.params = params;
    }

    public void createChatIcon(final EmiteDemoChatIconListener listener) {
	final PushButton icon = new PushButton(new Image("images/e-icon.gif"), new ClickHandler() {
	    public void onClick(ClickEvent event) {
		listener.onClick();
	    }
	});
	icon.setTitle("Click to show/hide dialog");
	RootPanel.get().add(icon, 320, 15);
    }

    public void createInfoPanel() {
	final String info = params.getInfo("info not found");
	if (info.length() > 0) {
	    final Panel infoPanel = new Panel();
	    infoPanel.setHeader(false);
	    infoPanel.setClosable(false);
	    infoPanel.setBorder(false);
	    infoPanel.setPaddings(15);
	    final FormPanel formPanel = new FormPanel();
	    formPanel.setFrame(true);
	    formPanel.setTitle("Info", "info-icon");
	    formPanel.setWidth(320);
	    final Label infoLabel = new Label();
	    infoLabel.setHtml(TextUtils.unescape(info));
	    formPanel.add(infoLabel);
	    infoPanel.add(formPanel);
	    RootPanel.get().add(infoPanel);
	}
    }

    public EmiteDemoLoginPanel createLoginPanel(final LoginPanelListener loginPanelListener) {
	final EmiteDemoLoginPanel emiteDemoLoginPanel = new EmiteDemoLoginPanel(loginPanelListener);
	emiteDemoLoginPanel.setInitalData(params.getJID(), params.getPassword(), params.getRelease());
	return emiteDemoLoginPanel;
    }

}
