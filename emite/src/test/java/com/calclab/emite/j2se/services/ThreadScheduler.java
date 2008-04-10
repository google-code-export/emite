package com.calclab.emite.j2se.services;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.services.ScheduledAction;

public class ThreadScheduler {

    public long getCurrentTime() {
	return System.currentTimeMillis();
    }

    public void schedule(final int msecs, final ScheduledAction action) {
	new Thread(new Runnable() {
	    public void run() {
		synchronized (this) {
		    try {
			Thread.sleep(msecs);
			action.run();
		    } catch (final InterruptedException e) {
			Log.debug("Scheduler exception: ", e);
		    }
		}
	    }
	}).start();
    }
}
