package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.RosterItem.Subscription;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class RosterManagerImpl implements RosterManager {

	private final Roster roster;

	private SubscriptionMode subscriptionMode;

	private final Signal<Presence> onSubscriptionRequested;

	private final Signal<XmppURI> onUnsubscribedReceived;

	private final Session session;

	private final Signal<Roster> onRosterReady;

	public RosterManagerImpl(final Session session, final Roster roster) {
		this.session = session;
		this.roster = roster;
		this.subscriptionMode = DEF_SUBSCRIPTION_MODE;
		this.onSubscriptionRequested = new Signal<Presence>(
				"rosterManager:onSubscriptionRequested");
		this.onUnsubscribedReceived = new Signal<XmppURI>(
				"rosterManager:onUnsubscribedReceived");
		this.onRosterReady = new Signal<Roster>("rosterManager:onRosterReady");

		session.onLoggedIn(new Slot<XmppURI>() {
			public void onEvent(final XmppURI parameter) {
				requestRoster();
			}
		});

		session.onLoggedOut(new Slot<XmppURI>() {
			public void onEvent(final XmppURI parameter) {
				roster.clear();
			}
		});

		session.onPresence(new Slot<Presence>() {
			public void onEvent(final Presence presence) {
				switch (presence.getType()) {
				case subscribe:
					handleSubscriptionRequest(presence);
					break;
				case unsubscribed:
					// Inform to user but not update roster (only iq set update
					// roster)
					handleUnsubscribedReceived(presence.getFrom());
					break;
				case subscribed:
					// Fine, but do nothing (only iq set update roster)
					break;
				case available:
				case unavailable:
					roster.changePresence(presence.getFrom(), presence);
					break;
				}
			}
		});

	}

	/**
	 * subscribed -- The sender has allowed the recipient to receive their
	 * presence.
	 */
	public void acceptSubscription(final Presence presence) {
		if (presence.getType() == Presence.Type.subscribe) {
			final XmppURI from = presence.getFrom();
			if (roster.findItemByJID(from.getJID()) == null) {
				final RosterItem item = new RosterItem(from, Subscription.none,
						from.getNode());
				roster.add(item);
			}
			final Presence response = new Presence(Presence.Type.subscribed,
					session.getCurrentUser(), from);
			session.send(response);
		} else {
			// throw exception: its a programming error
			throw new RuntimeException(
					"Trying to accept/deny a non subscription request");
		}
		requestSubscribe(presence.getFrom());

	}

	/**
	 * If a user would like to cancel a previously-granted subscription request,
	 * it sends a presence stanza of type "unsubscribed".
	 */
	public void cancelSubscriptor(final XmppURI to) {
		final Presence unsubscription = new Presence(
				Presence.Type.unsubscribed, session.getCurrentUser(), to);
		session.send(unsubscription);
	}

	/**
	 * unsubscribed -- The subscription request has been denied or a
	 * previously-granted subscription has been cancelled.
	 */
	public void denySubscription(final Presence presence) {
		if (presence.getType() == Presence.Type.subscribe) {
			final Presence response = new Presence(Presence.Type.unsubscribed,
					session.getCurrentUser(), presence.getFrom());
			session.send(response);
		} else {
			// throw exception: its a programming error
			throw new RuntimeException(
					"Trying to accept/deny a non subscription request");
		}
	}

	public SubscriptionMode getSubscriptionMode() {
		return subscriptionMode;
	}

	public void onRosterReady(final Slot<Roster> slot) {
		this.onRosterReady.add(slot);
	}

	public void onSubscriptionRequested(final Slot<Presence> listener) {
		onSubscriptionRequested.add(listener);
	}

	public void onUnsubscribedReceived(final Slot<XmppURI> listener) {
		onUnsubscribedReceived.add(listener);
	}

	public void requestAddItem(final XmppURI jid, final String name,
			final String group) {

		final IQ iq = new IQ(IQ.Type.set, session.getCurrentUser(), null);
		final IPacket item = iq.addQuery("jabber:iq:roster").addChild("item",
				null).With("jid", jid.toString()).With("name", name);

		if (group != null) {
			item.addChild("group", null).setText(group);
		}

		roster.add(new RosterItem(jid, Subscription.none, name));
		session.sendIQ("roster", iq, new Slot<IPacket>() {
			public void onEvent(final IPacket received) {
				if (IQ.isSuccess(received)) {
					final Presence presenceRequest = new Presence(
							Type.subscribe, null, jid);
					session.send(presenceRequest);
				} else {
					roster.removeItem(jid);
				}
			}
		});
	}

	public void requestRemoveItem(final XmppURI jid) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.addQuery("jabber:iq:roster").addChild("item", null).With("jid",
				jid.toString()).With("subscription", "remove");
		session.sendIQ("roster", iq, new Slot<IPacket>() {
			public void onEvent(final IPacket iq) {
				if (IQ.isSuccess(iq)) {
					roster.removeItem(jid);
				}
			}
		});
	}

	public void requestSubscribe(final XmppURI to) {
		final Presence unsubscribeRequest = new Presence(
				Presence.Type.subscribe, session.getCurrentUser(), to.getJID());
		session.send(unsubscribeRequest);
	}

	public void requestUnsubscribe(final XmppURI to) {
		final Presence unsubscribeRequest = new Presence(
				Presence.Type.unsubscribe, session.getCurrentUser(), to);
		session.send(unsubscribeRequest);
	}

	public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
		this.subscriptionMode = subscriptionMode;
	}

	private RosterItem convert(final IPacket item) {
		final String jid = item.getAttribute("jid");
		final XmppURI uri = uri(jid);
		final Subscription subscription = RosterItem.Subscription.valueOf(item
				.getAttribute("subscription"));
		return new RosterItem(uri, subscription, item.getAttribute("name"));
	}

	private List<? extends IPacket> getItems(final IPacket iPacket) {
		final List<? extends IPacket> items = iPacket.getFirstChild("query")
				.getChildren();
		return items;
	}

	private void handleSubscriptionRequest(final Presence presence) {
		switch (subscriptionMode) {
		case autoAcceptAll:
			acceptSubscription(presence);
			break;
		case autoRejectAll:
			denySubscription(presence);
			break;
		}
		onSubscriptionRequested.fire(presence);
	}

	private void handleUnsubscribedReceived(final XmppURI userUnsubscribed) {
		onUnsubscribedReceived.fire(userUnsubscribed);
	}

	private void requestRoster() {
		session.sendIQ("roster", new IQ(IQ.Type.get)
				.WithQuery("jabber:iq:roster"), new Slot<IPacket>() {
			public void onEvent(final IPacket received) {
				setRosterItems(roster, received);
			}

		});
	}

	private void setRosterItems(final Roster roster, final IPacket received) {
		final ArrayList<RosterItem> items = new ArrayList<RosterItem>();
		for (final IPacket item : getItems(received)) {
			items.add(convert(item));
		}
		roster.setItems(items);
		onRosterReady.fire(roster);
	}

}
