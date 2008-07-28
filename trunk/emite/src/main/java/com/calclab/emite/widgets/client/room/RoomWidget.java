package com.calclab.emite.widgets.client.room;

import com.calclab.emite.widgets.client.chat.GWTAbstractChatWidget;

public class RoomWidget extends GWTAbstractChatWidget {
    private RoomController controller;

    public RoomWidget() {
    }

    public String[] getParamNames() {
	return new String[] { "room", "nick" };
    }

    public void setParam(final String name, final String value) {
	if ("room".equals(name)) {
	    controller.setRoomJID(value);
	} else if ("nick".equals(name)) {
	    controller.setNick(value);
	}
    }

    void setController(final RoomController controller) {
	this.controller = controller;

    }

}
