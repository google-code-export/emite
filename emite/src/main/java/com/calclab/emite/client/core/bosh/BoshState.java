package com.calclab.emite.client.core.bosh;

import com.allen_sauer.gwt.log.client.Log;

public class BoshState {
	private int currentConnections;
	private boolean isLastResponseEmpty;
	private boolean isRunning;
	private boolean isTerminated;
	private long lastSendTime;
	private int poll;
	private long rid;
	private String sid;

	public BoshState() {
		init();
	}

	public void decreaseRequests() {
		currentConnections--;
	}

	public int getCurrentRequestsCount() {
		return currentConnections;
	}

	public int getPoll() {
		return poll;
	}

	public String getSID() {
		return sid;
	}

	public void increaseRequests() {
		currentConnections--;
	}

	public void init() {
		this.isRunning = false;
		rid = generateRID();
		this.sid = null;
		this.currentConnections = 0;
		this.poll = 1;
	}

	public boolean isFirstResponse() {
		return sid == null;
	}

	public boolean isLastResponseEmpty() {
		return isLastResponseEmpty;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public long nextRid() {
		rid++;
		return rid;
	}

	public void setLastResponseEmpty(final boolean isLastResponseEmpty) {
		this.isLastResponseEmpty = isLastResponseEmpty;

	}

	public void setLastSend(final long lastSendTime) {
		this.lastSendTime = lastSendTime;

	}

	public void setPoll(final int poll) {
		this.poll = poll;
	}

	public void setRunning(final boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void setSID(final String sid) {
		Log.debug("Setting SID!" + sid);
		this.sid = sid;
	}

	public void setTerminate() {
		this.isTerminated = true;

	}

	private long generateRID() {
		final long rid = (long) (Math.random() * 1245234);
		return rid;
	}

}
