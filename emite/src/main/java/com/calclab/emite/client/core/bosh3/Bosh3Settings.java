package com.calclab.emite.client.core.bosh3;

public class Bosh3Settings {
    public final String hostName;
    public final String httpBase;
    public final String version;
    public final int maxRequests;
    public final int hold;
    public final int wait;

    public Bosh3Settings(final String httpBase, final String hostName) {
	this(httpBase, hostName, "1.6", 2000, 1, 2);
    }

    public Bosh3Settings(final String httpBase, final String hostName, final String version, final int wait,
	    final int hold, final int maxRequests) {
	this.httpBase = httpBase;
	this.hostName = hostName;
	this.version = version;
	this.wait = wait;
	this.hold = hold;
	this.maxRequests = maxRequests;
    }

}
