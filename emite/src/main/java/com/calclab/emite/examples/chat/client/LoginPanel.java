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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel extends VerticalPanel {
    public static interface LoginPanelListener {
	void onLogin(String bind, String domain, String name, String password);
    }

    private final TextBox fieldBind;
    private final TextBox fieldDomain;
    private final TextBox fieldName;
    private final PasswordTextBox fieldPassword;
    private final Label labelStatus;

    public LoginPanel(final LoginPanelListener listener) {
	addStyleName("chatexample-login");
	labelStatus = (Label) addRow("", new Label("welcome to the example chat!"));
	fieldBind = (TextBox) addRow("bind: ", new TextBox());
	fieldDomain = (TextBox) addRow("domain: ", new TextBox());
	fieldName = (TextBox) addRow("user jid: ", new TextBox());
	fieldPassword = (PasswordTextBox) addRow("password: ", new PasswordTextBox());
	addRow(" ", new Button("login", new ClickListener() {
	    public void onClick(final Widget sender) {
		listener.onLogin(fieldBind.getText(), fieldDomain.getText(), fieldName.getText(), fieldPassword
			.getText());
	    }
	}));

	fieldBind.setText("/proxy");
	fieldDomain.setText("localhost");
    }

    public void setStatus(final String message) {
	labelStatus.setText(message);

    }

    private Widget addRow(final String label, final Widget widget) {
	final HorizontalPanel panel = new HorizontalPanel();
	panel.add(new Label(label));
	panel.add(widget);
	add(panel);
	return widget;
    }
}
