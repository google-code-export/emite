package com.calclab.hablar.demo.client;

import com.calclab.hablar.basic.client.ui.HablarWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DemoWidget extends Composite implements DemoView {

    interface DemoWidgetUiBinder extends UiBinder<Widget, DemoWidget> {
    }

    private static DemoWidgetUiBinder uiBinder = GWT.create(DemoWidgetUiBinder.class);

    @UiField
    HablarWidget hablar;

    @UiField
    TextBox userName;
    @UiField
    PasswordTextBox password;
    @UiField
    Button login, logout;
    @UiField
    Label state;

    public DemoWidget() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public HablarWidget getHablar() {
	return hablar;
    }

    @Override
    public HasClickHandlers getLogin() {
	return login;
    }

    @Override
    public HasClickHandlers getLogout() {
	return logout;
    }

    @Override
    public HasText getPassword() {
	return password;
    }

    @Override
    public HasText getState() {
	return state;
    }

    @Override
    public HasText getUserName() {
	return userName;
    }

    @Override
    public void setLoginEnabled(boolean enabled) {
	login.setEnabled(enabled);
    }

    @Override
    public void setLogoutEnabled(boolean enabled) {
	logout.setEnabled(enabled);
    }

}
