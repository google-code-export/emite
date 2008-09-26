package com.calclab.emite.core.client.bosh;

/**
 * Bosh connection settings
 */
public class BoshSettings {
    public final String hostName;
    public final String httpBase;
    public final String version;
    public final int maxRequests;
    public final int hold;
    public final int wait;

    public BoshSettings(final String httpBase, final String hostName) {
	this(httpBase, hostName, "1.6", 60, 1, 2);
    }

    public BoshSettings(final String httpBase, final String hostName, final String version, final int wait,
	    final int hold, final int maxRequests) {
	this.httpBase = httpBase;
	this.hostName = hostName;
	this.version = version;
	this.wait = wait;
	this.hold = hold;
	this.maxRequests = maxRequests;
    }

}
