package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.widgets.client.base.ComposedWidget;
import com.calclab.emite.widgets.client.base.DockableWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.logout.LogoutWidget;

/**
 * CharlaWidget is a one to one chat with login
 */
public class CharlaWidget extends ComposedWidget {
    public CharlaWidget(final LoginWidget login, final ChatWidget chat, final LogoutWidget logout) {
	super(login, chat);
	chat.dock(DockableWidget.EXT_TOP, logout);
    }

    @Override
    protected String[] getExtraParamNames() {
	return null;
    }

    @Override
    protected void setExtraParam(final String name, final String value) {
    }

}
