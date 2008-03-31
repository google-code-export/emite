package com.calclab.emite.client.core.services;

public interface Scheduler {
	long getCurrentTime();

	void schedule(int msecs, ScheduledAction action);
}
