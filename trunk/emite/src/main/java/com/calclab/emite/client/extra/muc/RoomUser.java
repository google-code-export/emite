package com.calclab.emite.client.extra.muc;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RoomUser {
    // FIXME: (Dani) Echa un vistazo a GroupChatUser extends AbstractUser (igual te vale para
    // algo)

    private final XmppURI uri;
    private final String affiliation;
    private final String role;

    public RoomUser(final XmppURI uri, final String affiliation, final String role) {
	this.uri = uri;
	this.affiliation = affiliation;
	this.role = role;
    }

    @Override
    public String toString() {
	return uri.toString() + "(" + affiliation + "," + role + ")";
    }
}
