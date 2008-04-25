package com.calclab.blackboard.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Controls extends HorizontalPanel {

    public static interface ControlsListener {
	void onLogin();
    }

    private final Button btnLogin;
    private final TextBox field;
    private boolean isLoggedIn;

    public Controls(final ControlsListener listener) {
	field = new TextBox();
	btnLogin = new Button("loading...", new ClickListener() {
	    public void onClick(final Widget sender) {
		if (isLoggedIn) {

		} else {
		    listener.onLogin();
		}
	    }
	});
	add(field);
	add(btnLogin);
    }

    public void setLoggedIn(final boolean isLoggedIn) {
	final String label = isLoggedIn ? "logout" : "login";
	btnLogin.setText(label);
	field.setEnabled(isLoggedIn);
	this.isLoggedIn = isLoggedIn;
    }

}
