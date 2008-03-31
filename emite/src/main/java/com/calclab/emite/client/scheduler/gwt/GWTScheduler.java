package com.calclab.emite.client.scheduler.gwt;

import java.util.Date;

import com.calclab.emite.client.scheduler.ScheduledAction;
import com.calclab.emite.client.scheduler.Scheduler;
import com.google.gwt.user.client.Timer;

public class GWTScheduler implements Scheduler {

	public long getCurrentTime() {
		return new Date().getTime();
	}

	public void schedule(final int msecs, final ScheduledAction action) {
		new Timer() {
			@Override
			public void run() {
				action.run();
			}
		}.schedule(msecs);
	}

}
