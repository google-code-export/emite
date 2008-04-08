package com.calclab.emite.client.extra.muc;

public class Room {
    private final String jid;
    private final String name;

    public Room(final String JID, final String name) {
	jid = JID;
	this.name = name;
    }

    public String getJid() {
	return jid;
    }

    public String getName() {
	return name;
    }

}
