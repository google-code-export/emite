package com.calclab.emite.widgets.client.login;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Event2;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
	this.button = new Button("login", new ClickListener() {
	    public void onClick(final Widget arg0) {
		if (isConnected)
		    onLogout.fire();
		else
		    onLogin.fire(jid.getText(), password.getText());
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
