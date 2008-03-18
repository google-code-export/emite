package com.calclab.emite.client.x.im.session;

import com.calclab.emite.client.x.im.session.Session.State;

public interface SessionListener {
	void onStateChanged(State old, State current);
}
