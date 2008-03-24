package com.calclab.emite.client.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public class DispatcherDefault implements Dispatcher {
	private final Logger logger;
	private final Parser parser;
	private final HashMap<String, List<Action>> subscriptors;
	private final ArrayList<Packet> queue;
	private boolean isRunning;
	private final DispatcherStateListenerCollection listeners;

	DispatcherDefault(final Parser parser, final Logger logger) {
		this.parser = parser;
		this.logger = logger;
		this.subscriptors = new HashMap<String, List<Action>>();
		this.listeners = new DispatcherStateListenerCollection();
		this.queue = new ArrayList<Packet>();
		this.isRunning = false;
	}

	public void addListener(final DispatcherStateListener listener) {
		listeners.add(listener);
	}

	public void publish(final Packet packet) {
		queue.add(packet);
		if (!isRunning)
			start();
	}

	public void subscribe(final Matcher matcher, final Action action) {
		final List<Action> list = getSubscriptorList(matcher.getElementName());
		list.add(new FilteredAction(matcher, action));
	}

	private void fireActions(final Packet packet,
			final List<Action> subscriptors) {

		for (final Action action : subscriptors) {
			action.handle(packet);
		}
	}

	private List<Action> getSubscriptorList(final String name) {
		List<Action> list = subscriptors.get(name);
		if (list == null) {
			list = new ArrayList<Action>();
			subscriptors.put(name, list);
		}
		return list;
	}

	/**
	 * TODO: possible race condition
	 */
	private void start() {
		listeners.fireBeforeDispatch();
		isRunning = true;
		while (queue.size() > 0) {
			final Packet next = queue.remove(0);
			fireActions(next, getSubscriptorList(next.getName()));
		}
		isRunning = false;
		listeners.fireAfterDispatch();
	}

}
