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
