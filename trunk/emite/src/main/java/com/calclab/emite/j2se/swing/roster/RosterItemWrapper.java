/**
 * 
 */
package com.calclab.emite.j2se.swing.roster;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.im.client.roster.RosterItem;

public class RosterItemWrapper {
    final RosterItem item;
    final String name;

    public RosterItemWrapper(final String name, final RosterItem item) {
        this.item = item;
        this.name = name;
    }

    @Override
    public String toString() {
        final Presence presence = item.getPresence();
        final String status = " - " + presence.getType() + ":" + presence.getShow();
        return name + "(" + item.getJID() + ") - " + presence.getStatus() + status;
    }
}