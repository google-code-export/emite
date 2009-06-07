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
package com.calclab.emite.widgets.client.login;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Event2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Simple login widget
 * 
 * @html_param jid: The default JID
 * @html_param password: the default password
 * 
 */
public class LoginWidget extends VerticalPanel implements EmiteWidget {
    private final TextBox jid;
    private final PasswordTextBox password;
    private final Button button;
    final Event2<String, String> onLogin;
    private final Label status;
    final Event0 onLogout;
    private boolean isConnected;
    private final Label error;

    public LoginWidget() {
	setStylePrimaryName("emite-LoginWidget");
	this.isConnected = false;
	this.onLogin = new Event2<String, String>("widgets:login:onLogin");
	this.onLogout = new Event0("widgets:login:onLogout");
	this.jid = new TextBox();
	this.password = new PasswordTextBox();
	this.button = new Button("login", new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		if (isConnected) {
		    onLogout.fire();
		} else {
		    onLogin.fire(jid.getText(), password.getText());
		}
	    }
	});
	this.status = new Label();
	this.error = new Label("errors here!");
	error.addStyleDependentName("error");

	add(error);
	add(jid);
	add(password);
	add(button);
	add(status);
    }

    public String[] getParamNames() {
	return new String[] { "jid", "password" };
    }

    public void setButtonBehaviour(final boolean isConnected, final String label) {
	this.isConnected = isConnected;
	button.setText(label);
    }

    public void setButtonEnabled(final boolean enabled) {
	button.setEnabled(enabled);
    }

    public void setFieldsEnabled(final boolean enabled) {
	jid.setEnabled(enabled);
	password.setEnabled(enabled);
    }

    public void setParam(final String name, final String value) {
	if ("jid".equals(name)) {
	    jid.setText(value);
	} else if ("password".equals(name)) {
	    password.setText(value);
	}
    }

    public void showError(final String errorMessage) {
	if (errorMessage == null || errorMessage.length() == 0) {
	    error.setVisible(false);
	} else {
	    error.setText(errorMessage);
	    error.setVisible(true);
	}
    }

    public void showMessage(final String statusMessage) {
	status.setText(statusMessage);
    }

}
