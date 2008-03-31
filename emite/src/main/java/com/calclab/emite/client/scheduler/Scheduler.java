package com.calclab.emite.client.scheduler;

public interface Scheduler {
	long getCurrentTime();

	void schedule(int msecs, ScheduledAction action);
}
