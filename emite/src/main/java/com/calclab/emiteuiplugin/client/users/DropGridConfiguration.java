package com.calclab.emiteuiplugin.client.users;

public class DropGridConfiguration {

    private final String ddGroupId;
    private final UserGridDropListener listener;

    public DropGridConfiguration(final String ddGroupId, final UserGridDropListener listener) {
	this.ddGroupId = ddGroupId;
	this.listener = listener;
    }

    public String getDdGroupId() {
	return ddGroupId;
    }

    public UserGridDropListener getListener() {
	return listener;
    }

}
