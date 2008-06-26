package com.calclab.emite.client.core.bosh2;

public class B2BoshOptions {
    public final String httpBase;
    public final String domain;
    public final String version;
    public final String wait;
    public final String hold;

    public B2BoshOptions(final String httpBase, final String domain) {
	this(httpBase, domain, "1.6", 60, 1);
    }

    public B2BoshOptions(final String httpBase, final String domain, final String version, final int wait,
	    final int hold) {
	this.httpBase = httpBase;
	this.domain = domain;
	this.version = version;
	this.wait = "" + wait;
	this.hold = "" + hold;
    }

}
