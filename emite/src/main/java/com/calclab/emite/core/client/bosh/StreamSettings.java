package com.calclab.emite.core.client.bosh;

public class StreamSettings {
    public long rid;
    public String sid;
    public String wait;
    public String inactivity;
    public String maxPause;
    long lastRequestTime;

    public StreamSettings() {
	this.rid = (long) (Math.random() * 10000000) + 1000;
    }

    public String getNextRid() {
	rid++;
	return "" + rid;
    }

}
