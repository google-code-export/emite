package com.calclab.emiteuiplugin.client.users;

public class DragGridConfiguration {

    private final String ddGroupId;
    private final String dragMessage;

    public DragGridConfiguration(final String ddGroupId, final String dragMessage) {
	this.ddGroupId = ddGroupId;
	this.dragMessage = dragMessage;
    }

    public String getDdGroupId() {
	return ddGroupId;
    }

    public String getDragMessage() {
	return dragMessage;
    }

}
