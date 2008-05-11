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
package com.calclab.emite.client.core.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;

public class DispatcherDefault implements Dispatcher {
    private static class Subscriptor {
	final Matcher matcher;
	final PacketListener packetListener;

	public Subscriptor(final Matcher matcher, final PacketListener packetListener) {
	    this.matcher = matcher;
	    this.packetListener = packetListener;
	}

    }

    private boolean isCurrentlyDispatching;
    private final DispatcherStateListenerCollection listeners;

    private final ArrayList<IPacket> queue;
    private final HashMap<String, List<Subscriptor>> subscriptors;

    public DispatcherDefault() {
	this.subscriptors = new HashMap<String, List<Subscriptor>>();
	subscriptors.put(null, new ArrayList<Subscriptor>());
	this.listeners = new DispatcherStateListenerCollection();
	this.queue = new ArrayList<IPacket>();
	this.isCurrentlyDispatching = false;
    }

    public void addListener(final DispatcherStateListener listener) {
	listeners.add(listener);
    }

    public void publish(final IPacket iPacket) {
	Log.debug("Dispatching: " + iPacket);
	queue.add(iPacket);
	startIfNeeded();
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	final List<Subscriptor> list = getSubscriptorList(matcher.getElementName());
	list.add(new Subscriptor(matcher, packetListener));
    }

    private void fireActions(final IPacket iPacket, final List<Subscriptor> subscriptors) {
	for (final Subscriptor subscriptor : subscriptors) {
	    if (subscriptor.matcher.matches(iPacket)) {
		subscriptor.packetListener.handle(iPacket);
	    }
	}
    }

    private void fireErrorEvent(final Exception t) {
	this.queue.clear();
	Log.debug("ERROR EVENT" + t.toString());
	final Event error = Events.error("exception", t.getMessage());
	final List<Subscriptor> eventSubscriptors = subscriptors.get(error.getName());
	for (final Subscriptor subscriptor : eventSubscriptors) {
	    try {
		if (subscriptor.matcher.matches(error)) {
		    subscriptor.packetListener.handle(error);
		}
	    } catch (final Exception e) {
	    }
	}

    }

    private List<Subscriptor> getSubscriptorList(final String name) {
	List<Subscriptor> list = subscriptors.get(name);
	if (list == null) {
	    list = new ArrayList<Subscriptor>();
	    subscriptors.put(name, list);
	}
	return list;
    }

    // FIXME: investigar este sync
    private synchronized void start() {
	isCurrentlyDispatching = true;
	listeners.fireBeforeDispatch();
	try {
	    while (queue.size() > 0) {
		final IPacket next = queue.remove(0);
		fireActions(next, subscriptors.get(null));
		fireActions(next, getSubscriptorList(next.getName()));
	    }
	} catch (final RuntimeException e) {
	    fireErrorEvent(e);
	    throw e;
	} catch (final Exception exception) {
	    fireErrorEvent(exception);
	}
	isCurrentlyDispatching = false;
	listeners.fireAfterDispatch();
    }

    private void startIfNeeded() {
	if (!isCurrentlyDispatching) {
	    start();
	}
    }

}
