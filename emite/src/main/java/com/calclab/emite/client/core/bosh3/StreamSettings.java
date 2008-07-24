package com.calclab.emite.client.core.bosh3;

class StreamSettings {
    final String sid;
    final String wait;
    final String inactivity;
    final String maxpause;

    public StreamSettings(final String sid, final String wait, final String inactivity, final String maxpause) {
	this.sid = sid;
	this.wait = wait;
	this.inactivity = inactivity;
	this.maxpause = maxpause;
    }

}
