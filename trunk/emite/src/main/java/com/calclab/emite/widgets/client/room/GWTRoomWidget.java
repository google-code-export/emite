package com.calclab.emite.widgets.client.room;

import com.calclab.emite.widgets.client.chat.GWTAbstractChatWidget;

public class GWTRoomWidget extends GWTAbstractChatWidget implements RoomWidget {

    public static final String PARAM_NICK = "nick";
    public static final String PARAM_ROOM = "room";
    public static final String PARAM_PRESENCE = "presence";

    public GWTRoomWidget() {
	setStylePrimaryName("emite-RoomWidget");
    }

    public String[] getParamNames() {
	return new String[] { PARAM_ROOM, PARAM_NICK, PARAM_PRESENCE };
    }

    public void setParam(final String name, final String value) {
	final RoomController controller = (RoomController) getController();
	if (PARAM_ROOM.equals(name)) {
	    controller.setRoomJID(value);
	} else if (PARAM_NICK.equals(name)) {
	    controller.setNick(value);
	}
	if (PARAM_PRESENCE.equals(name)) {
	    final boolean showPresnece = "true".equals(value);
	    controller.showPresence(showPresnece);
	}
    }

}
