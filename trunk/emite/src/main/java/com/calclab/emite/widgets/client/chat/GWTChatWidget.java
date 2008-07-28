package com.calclab.emite.widgets.client.chat;

public class GWTChatWidget extends GWTAbstractChatWidget implements ChatWidget {

    private ChatController controller;

    public String[] getParamNames() {
	return new String[] { "chat" };
    }

    public void setController(final ChatController controller) {
	this.controller = controller;
    }

    public void setParam(final String name, final String value) {
	if ("chat".equals(name)) {
	    controller.setChatJID(value);
	}
    }

}
