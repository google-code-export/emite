package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.xmpp.session.Session.State;

public interface SessionListener {
	void onStateChanged(State old, State current);
}
