package com.calclab.emite.client.bosh;

import com.calclab.emite.client.scheduler.ScheduledAction;

public class Activator implements ScheduledAction {
	private final Bosh bosh;

	Activator(final Bosh bosh) {
		this.bosh = bosh;
	}

	public void run() {
		bosh.send();
	}

}
