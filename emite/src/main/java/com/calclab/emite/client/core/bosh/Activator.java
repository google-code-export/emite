package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.scheduler.ScheduledAction;

public class Activator implements ScheduledAction {
	private final Bosh bosh;
	private boolean isCancelled;

	Activator(final Bosh bosh) {
		this.bosh = bosh;
		this.isCancelled = false;
	}

	public void cancel() {
		this.isCancelled = true;
	}

	public void run() {
		if (!isCancelled) {
			bosh.sendResponse();
		}
	}
}
