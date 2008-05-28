/**
 *
 */
package com.calclab.emite.client.core.bosh;

public class BoshState {
    public static final BoshState IGNORE = new BoshState(-1);
    public static final BoshState SEND = new BoshState(0);

    public static BoshState shouldWait(final int time) {
	return new BoshState(time);
    }

    private final int time;

    private BoshState(final int time) {
	this.time = time;
    }

    public int getTime() {
	return time;
    }

    public boolean shouldIgnore() {
	return time < 0;
    }

    public boolean shouldSend() {
	return time == 0;
    }

    public boolean shouldWait() {
	return time > 0;
    }
}