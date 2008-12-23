package com.calclab.emite.hablar.client.pages;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.hablar.client.PageView;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainPage extends PageView {
    private final Event0 onLogout;
    private final Event2<String, String> onLogin;

    private Label message;
    private TextBox user;
    private PasswordTextBox password;
    private final Button btnLogin;
    private final Button btnLogout;
    private ListBox selectShow;
    private TextBox fieldStatus;

    public MainPage() {
	onLogin = new Event2<String, String>("loginView:login");
	onLogout = new Event0("loginView:logout");

	btnLogin = new Button("login", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onLogin.fire(user.getText(), password.getText());
	    }
	});
	btnLogout = new Button("logout", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onLogout.fire();
	    }

	});
	getToolbar().add(btnLogin);
	getToolbar().add(btnLogout);

	addContent(createStatus());
	addContent(createForm());

	user.setText("test1@localhost");
	password.setText("test1");
    }

    public void onLogin(final Listener2<String, String> listener) {
	onLogin.add(listener);
    }

    public void onLogout(final Listener0 listener) {
	onLogout.add(listener);
    }

    public void setLoginEnabled(final boolean enabled) {
	btnLogin.setEnabled(enabled);
    }

    public void setLogoutEnabled(final boolean enabled) {
	btnLogout.setEnabled(enabled);
    }

    public void show(final String text) {
	message.setText(text);
    }

    private VerticalPanel createForm() {
	final VerticalPanel form = new VerticalPanel();
	form.setSpacing(5);

	form.add(new Label("User name:"));
	user = new TextBox();
	form.add(user);
	form.add(new Label("Password:"));
	password = new PasswordTextBox();
	form.add(password);

	message = new Label();
	form.add(message);

	return form;
    }

    private Widget createStatus() {
	final VerticalPanel panel = new VerticalPanel();
	panel.add(new Label("Status:"));
	selectShow = new ListBox(false);
	selectShow.addItem("Available", Presence.Show.chat.toString());
	selectShow.addItem("Busy", Presence.Show.dnd.toString());
	panel.add(selectShow);
	panel.add(new Label("Message:"));
	fieldStatus = new TextBox();
	panel.add(fieldStatus);

	return panel;
    }

}
