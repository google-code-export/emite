package com.calclab.widgets.comenta.client.ui;

import com.calclab.suco.client.signal.Slot2;
import com.google.gwt.user.client.ui.DeckPanel;

public class ComentaWidget extends DeckPanel {
    private final LoginPanel loginPanel;

    public ComentaWidget() {
	loginPanel = new LoginPanel();
	this.add(loginPanel);
	showWidget(0);
    }

    public void onLogin(final Slot2<String, String> slot) {
	loginPanel.onLogin.add(slot);
    }

    public void showStatus(final String statusMessage) {
	loginPanel.showMessage(statusMessage);
    }
}
