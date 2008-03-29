package com.calclab.emite.client.scheduler;

import com.allen_sauer.gwt.log.client.Log;

public class ThreadScheduler implements Scheduler {

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
