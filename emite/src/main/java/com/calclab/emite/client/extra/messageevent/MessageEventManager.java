/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.extra.messageevent;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.xmpp.session.SessionComponent;

/**
 * XEP-0022: Message Events implementation
 * 
 */
public class MessageEventManager extends SessionComponent {

    public static class Events {

        /**
         * Indicates that the message has been stored offline by the intended
         * recipient's server. This event is triggered only if the intended
         * recipient's server supports offline storage, has that support
         * enabled, and the recipient is offline when the server receives the
         * message for delivery. *
         */
        public static final Event offline = new Event("messageevent:on:offline");

        /**
         * Indicates that the message has been delivered to the recipient. This
         * signifies that the message has reached the recipient's Jabber client,
         * but does not necessarily mean that the message has been displayed.
         * This event is to be raised by the Jabber client.
         */
        public static final Event delivered = new Event("messageevent:on:delivered");

        /**
         * Once the message has been received by the recipient's Jabber client,
         * it may be displayed to the user. This event indicates that the
         * message has been displayed, and is to be raised by the Jabber client.
         * Even if a message is displayed multiple times, this event should be
         * raised only once.
         */
        public static final Event displayed = new Event("messageevent:on:displayed");

        /**
         * In threaded chat conversations, this indicates that the recipient is
         * composing a reply to a message. The event is to be raised by the
         * recipient's Jabber client. A Jabber client is allowed to raise this
         * event multiple times in response to the same request, providing the
         * original event is cancelled first.
         */
        public static final Event composing = new Event("messageevent:on:composing");
    }

    public MessageEventManager(final Emite emite) {
        super(emite);
    }

}
