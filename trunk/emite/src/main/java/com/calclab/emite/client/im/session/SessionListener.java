package com.calclab.emite.client.im.session;

import com.calclab.emite.client.im.session.Session.State;

public interface SessionListener {
	void onStateChanged(State old, State current);
}
