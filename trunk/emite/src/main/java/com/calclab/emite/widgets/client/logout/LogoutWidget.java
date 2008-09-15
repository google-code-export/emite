package com.calclab.emite.widgets.client.logout;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.listener.Event0;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LogoutWidget extends HorizontalPanel implements EmiteWidget {

    private final Label label;
    private final Button button;
    final Event0 onLogout;

    public LogoutWidget() {
	setStylePrimaryName("emite-LogoutWidget");
	this.onLogout = new Event0("widgets:logout:onLogout");
	this.label = new Label();
	this.button = new Button("logout", new ClickListener() {
	    public void onClick(final Widget sender) {
		onLogout.fire();
	    }
	});
	add(label);
	add(button);
    }

    public String[] getParamNames() {
	return null;
    }

    public void setButtonVisible(final boolean visible) {
	button.setVisible(visible);
    }

    public void setParam(final String name, final String value) {
    }

    public void showMessage(final String message) {
	label.setText(message);
    }

}
