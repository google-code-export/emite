package com.calclab.emite.widgets.client.room;

import com.calclab.emite.widgets.client.base.ComposedWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;

public class ComentaWidget extends ComposedWidget {

    public ComentaWidget(final LoginWidget login, final RoomWidget roomWidget) {
	super(login, roomWidget);
    }

    @Override
    protected String[] getExtraParamNames() {
	return null;
    }

    @Override
    protected void setExtraParam(final String name, final String value) {
    }

}
