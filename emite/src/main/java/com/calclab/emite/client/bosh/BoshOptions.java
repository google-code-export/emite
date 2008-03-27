package com.calclab.emite.client.bosh;

public class BoshOptions {
	private final String domain;
	private final int hold;
	private final String httpBase;
	private final String resource;
	private final String version;
	private final int wait;

	public BoshOptions(final String httpBase, final String domain) {
		this(httpBase, domain, "emite", "1.6", 2000, 1);
	}

	public BoshOptions(final String httpBase, final String domain, final String resource, final String version,
			final int wait, final int hold) {
		this.httpBase = httpBase;
		this.domain = domain;
		this.resource = resource;
		this.version = version;
		this.wait = wait;
		this.hold = hold;
	}

	public String getDomain() {
		return domain;
	}

	public int getHold() {
		return hold;
	}

	public String getHttpBase() {
		return httpBase;
	}

	public String getResource() {
		return resource;
	}

	public String getVersion() {
		return version;
	}

	public int getWait() {
		return wait;
	}

}
