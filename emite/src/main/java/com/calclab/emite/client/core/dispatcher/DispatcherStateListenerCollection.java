package com.calclab.emite.client.core.dispatcher;

import java.util.ArrayList;

@SuppressWarnings("serial")
class DispatcherStateListenerCollection extends
		ArrayList<DispatcherStateListener> {

	public void fireAfterDispatch() {
		for (final DispatcherStateListener listener : this) {
			listener.afterDispatching();
		}
	}

	public void fireBeforeDispatch() {
		for (final DispatcherStateListener listener : this) {
			listener.beforeDispatching();
		}
	}

}
