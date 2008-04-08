package com.calclab.emite.j2se.scheduler;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Scheduler;

public class ThreadScheduler implements Scheduler {

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public void schedule(final int msecs, final ScheduledAction action) {
		Log.debug("Schedule action at " + msecs + " from " + System.currentTimeMillis());
		new Thread(new Runnable() {
			public void run() {
				synchronized (this) {
					try {
						Thread.sleep(msecs);
						Log.debug("Running action at: " + System.currentTimeMillis());
						action.run();
					} catch (final InterruptedException e) {
						Log.debug("Scheduler exception: ", e);
					}
				}
			}
		}).start();
	}
}
