package com.calclab.emite.widgets.client.chat;

public class GWTChatWidget extends GWTAbstractChatWidget implements ChatWidget {

    public String[] getParamNames() {
	return new String[] { ChatWidget.PARAM_CHAT };
    }

    public void setParam(final String name, final String value) {
	if (ChatWidget.PARAM_CHAT.equals(name)) {
	    final ChatController controller = (ChatController) getController();
	    controller.setChatJID(value);
	}
    }

}
