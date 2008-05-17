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
package com.calclab.emiteui.client.demo;

import com.calclab.emite.client.core.packet.TextUtils;
import com.calclab.emiteui.client.DemoParameters;
import com.calclab.emiteui.client.demo.LoginPanel.LoginPanelListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;

public class EmiteDemoUI {

    private final DemoParameters params;

    public EmiteDemoUI(final DemoParameters params) {
	this.params = params;
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

    public LoginPanel createLoginPanel(final LoginPanelListener loginPanelListener) {
	final LoginPanel loginPanel = new LoginPanel(loginPanelListener);
	loginPanel.setInitalData(params.getJID(), params.getPassword(), params.getRelease());
	return loginPanel;
    }

}
