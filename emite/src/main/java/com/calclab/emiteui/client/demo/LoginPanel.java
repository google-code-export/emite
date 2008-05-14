package com.calclab.emiteui.client.demo;

import java.util.Date;

import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class LoginPanel {

    public static interface LoginPanelListener {

	void onUserChanged(UserChatOptions generateUserChatOptions);

    }

    private final TextField fieldJid;
    private final TextField fieldPassw;
    private String release;

    public LoginPanel(final LoginPanelListener listener) {
	release = "not-specified";
	final Panel panel = new Panel();
	panel.setBorder(false);
	panel.setPaddings(15);

	final FormPanel formPanel = new FormPanel();
	formPanel.setFrame(true);
	formPanel.setTitle("Some external Login Form");

	formPanel.setWidth(320);
	formPanel.setLabelWidth(75);

	fieldJid = new TextField("Jabber id", "jid", 200);
	fieldJid.setAllowBlank(false);
	formPanel.add(fieldJid);

	fieldPassw = new TextField("Password", "last", 200);
	fieldPassw.setAllowBlank(false);
	fieldPassw.setPassword(true);
	formPanel.add(fieldPassw);

	fieldJid.addListener(new FieldListenerAdapter() {
	    @Override
	    public void onChange(final Field field, final Object newVal, final Object oldVal) {
		listener.onUserChanged(getUserChatOptions());
	    }
	});

	fieldPassw.addListener(new FieldListenerAdapter() {
	    @Override
	    public void onChange(final Field field, final Object newVal, final Object oldVal) {
		listener.onUserChanged(getUserChatOptions());
	    }
	});
	panel.add(formPanel);

	RootPanel.get().add(panel);
    }

    public UserChatOptions getUserChatOptions() {
	final String resource = "emiteui-" + new Date().getTime() + "-" + release;
	return new UserChatOptions(fieldJid.getRawValue(), fieldPassw.getRawValue(), resource, "blue",
		RosterManager.DEF_SUBSCRIPTION_MODE);
    }

    public void setInitalData(final String djid, final String pass, final String relVer) {
	this.release = relVer;
	fieldPassw.setValue(pass);
	fieldJid.setValue(djid);
    }
}
