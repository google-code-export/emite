package com.calclab.emite.widgets.client.login;

import com.calclab.emite.widgets.client.EmiteWidget;
import com.calclab.suco.client.signal.Signal0;
import com.calclab.suco.client.signal.Signal2;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginWidget extends VerticalPanel implements EmiteWidget {
    private final TextBox jid;
    private final PasswordTextBox password;
    private final Button login;
    final Signal2<String, String> onLogin;
    private final Label status;
    private final Button logout;
    final Signal0 onLogout;

    public LoginWidget() {
	this.onLogin = new Signal2<String, String>("comenta:onLogin");
	this.onLogout = new Signal0("comenta:onLogout");
	this.jid = new TextBox();
	this.password = new PasswordTextBox();
	this.login = new Button("login", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onLogin.fire(jid.getText(), password.getText());
	    }
	});
	this.logout = new Button("logout", new ClickListener() {
	    public void onClick(final Widget sender) {
		onLogout.fire();
	    }
	});
	this.status = new Label();
	add(jid);
	add(password);
	add(login);
	add(logout);
	add(status);
    }

    public void setFieldsEnabled(final boolean enabled) {
	jid.setEnabled(enabled);
	password.setEnabled(enabled);
    }

    public void setLoginEnabled(final boolean enabled) {
	login.setEnabled(enabled);
    }

    public void setLogoutEnabled(final boolean enabled) {
	logout.setEnabled(enabled);

    }

    public void showMessage(final String statusMessage) {
	status.setText(statusMessage);
    }
}
