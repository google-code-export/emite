package com.calclab.emite.client.xep.muc;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RoomInvitation {
    private final XmppURI invitor;
    private final XmppURI roomURI;
    private final String reason;

    public RoomInvitation(final XmppURI invitor, final XmppURI roomURI, final String reason) {
        this.invitor = invitor;
        this.roomURI = roomURI;
        this.reason = reason;
    }

    public XmppURI getInvitor() {
        return invitor;
    }

    public String getReason() {
        return reason;
    }

    public XmppURI getRoomURI() {
        return roomURI;
    }
}
