package com.calclab.emite.client.x.im.roster;

import java.util.List;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class RosterPlugin extends SenderPlugin {

    public static Roster getRoster(final Components components) {
        return (Roster) components.get("roster");
    }

    final IQ requestRoster;
    final Roster roster;

    final Action setRosterItems;

    public RosterPlugin(final Connection connection) {
        super(connection);
        roster = new Roster();

        requestRoster = new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster");

        setRosterItems = new Action() {
            public void handle(final Packet packet) {
                roster.clear();
                for (final Packet item : getItems(packet)) {
                    roster.add(convert(item));
                }
            }

            private RosterItem convert(final Packet item) {
                return new RosterItem(item.getAttribute("jid"), item.getAttribute("subscription"), item
                        .getAttribute("name"));
            }

            private List<Packet> getItems(final Packet packet) {
                final List<Packet> items = packet.getFirstChildren("query").getChildren("item");
                return items;
            }
        };
    }

    @Override
    public void attach() {
        when.Event(SessionPlugin.Events.started).Send(requestRoster);
        when.IQ("roster").Do(setRosterItems);
    }

    @Override
    public void install() {
        register("roster", roster);
    }

}
