package com.calclab.widgets.comenta.client.ui;

import com.calclab.suco.client.signal.Signal2;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class LoginPanel extends VerticalPanel {
    private final TextBox name;
    private final PasswordTextBox password;
    private final Button login;
    final Signal2<String, String> onLogin;
    private final Label status;

    LoginPanel() {
	this.onLogin = new Signal2<String, String>("comenta:onLogin");
	this.name = new TextBox();
	this.password = new PasswordTextBox();
	this.login = new Button("login", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onLogin.fire(name.getText(), password.getText());
	    }
	});
	this.status = new Label();
	add(name);
	add(password);
	add(login);
	add(status);
    }

    public void showMessage(final String statusMessage) {
	status.setText(statusMessage);
    }

}
