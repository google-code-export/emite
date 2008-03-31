package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.services.ScheduledAction;

public class Activator implements ScheduledAction {
	private final BoshManager boshManager;
	private boolean isCancelled;

	Activator(final BoshManager boshManager) {
		this.boshManager = boshManager;
		this.isCancelled = false;
	}

	public void cancel() {
		this.isCancelled = true;
	}

	public void run() {
		if (!isCancelled) {
			boshManager.sendResponse();
		}
	}
}
