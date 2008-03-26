package com.calclab.emite.client.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public class DispatcherDefault implements Dispatcher {
	private static class Subscriptor {
		private final Matcher matcher;
		private final Action action;

		public Subscriptor(final Matcher matcher, final Action action) {
			this.matcher = matcher;
			this.action = action;
		}

	}

	private final HashMap<String, List<Subscriptor>> subscriptors;
	private final ArrayList<Packet> queue;
	private boolean isCurrentlyDispatching;

	private final DispatcherStateListenerCollection listeners;

	DispatcherDefault() {
		this.subscriptors = new HashMap<String, List<Subscriptor>>();
		this.listeners = new DispatcherStateListenerCollection();
		this.queue = new ArrayList<Packet>();
		this.isCurrentlyDispatching = false;
	}

	public void addListener(final DispatcherStateListener listener) {
		listeners.add(listener);
	}

	public void publish(final Packet packet) {
		Log.debug("published: " + packet);
		queue.add(packet);
		if (!isCurrentlyDispatching) {
			start();
		}
	}

	public void subscribe(final Matcher matcher, final Action action) {
		Log.debug("Subscribing to {0}" + matcher.getElementName());
		final List<Subscriptor> list = getSubscriptorList(matcher
				.getElementName());
		list.add(new Subscriptor(matcher, action));
	}

	private void fireActions(final Packet packet,
			final List<Subscriptor> subscriptors) {
		Log.debug("Found " + subscriptors.size() + " subscriptors to "
				+ packet.getName());
		for (final Subscriptor subscriptor : subscriptors) {
			if (subscriptor.matcher.matches(packet)) {
				Log.debug("Subscriptor found!");
				subscriptor.action.handle(packet);
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

	/**
	 * TODO: possible race condition under J2SE
	 */
	private void start() {
		listeners.fireBeforeDispatch();
		isCurrentlyDispatching = true;
		Log.debug("begin dispatch loop");
		while (queue.size() > 0) {
			final Packet next = queue.remove(0);
			Log.debug("dispatching: " + next);
			fireActions(next, getSubscriptorList(next.getName()));
		}
		isCurrentlyDispatching = false;
		Log.debug("end dispatch loop");
		listeners.fireAfterDispatch();
	}

}
