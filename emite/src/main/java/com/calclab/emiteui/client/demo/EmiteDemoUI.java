package com.calclab.emiteui.client.demo;

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.ExtensibleWidgetChild;

import com.calclab.emite.client.core.packet.TextUtils;
import com.calclab.emiteui.client.DemoParameters;
import com.calclab.emiteui.client.demo.LoginPanel.LoginPanelListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;

public class EmiteDemoUI {

    private final DemoParameters params;

    public EmiteDemoUI(final DemoParameters params) {
	this.params = params;
    }

    public void createInfoPanel() {
	final String info = params.getInfo("info not found");
	if (info.length() > 0) {
	    final Panel infoPanel = new Panel();
	    infoPanel.setHeader(false);
	    infoPanel.setClosable(false);
	    infoPanel.setBorder(false);
	    infoPanel.setPaddings(15);
	    final FormPanel formPanel = new FormPanel();
	    formPanel.setFrame(true);
	    formPanel.setTitle("Info", "info-icon");
	    formPanel.setWidth(320);
	    final Label infoLabel = new Label();
	    infoLabel.setHtml(TextUtils.unescape(info));
	    formPanel.add(infoLabel);
	    infoPanel.add(formPanel);
	    RootPanel.get().add(infoPanel);
	}
    }

    public LoginPanel createLoginPanel(final LoginPanelListener loginPanelListener) {
	final LoginPanel loginPanel = new LoginPanel(loginPanelListener);
	loginPanel.setInitalData(params.getJID(), params.getPassword(), params.getRelease());
	return loginPanel;
    }

    public void createShowHideButton() {
	DefaultDispatcher.getInstance().subscribe(PlatformEvents.ATTACH_TO_EXTENSIBLE_WIDGET,
		new Action<ExtensibleWidgetChild>() {
		    public void execute(final ExtensibleWidgetChild extChild) {
			RootPanel.get().add((Widget) extChild.getView(), 320, 15);
		    }
		});
    }
}
