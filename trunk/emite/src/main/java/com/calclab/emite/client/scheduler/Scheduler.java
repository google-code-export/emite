package com.calclab.emite.client.scheduler;

public interface Scheduler {
	void schedule(int msecs, ScheduledAction action);
}
