package com.calclab.emite.client.core.bosh2;

public class B2Error {
    public final String name;
    public final String cause;
    public final Throwable exception;

    public B2Error(final String name, final String cause) {
	this(name, cause, null);
    }

    public B2Error(final String name, final String cause, final Throwable exception) {
	this.name = name;
	this.cause = cause;
	this.exception = exception;
    }

    public B2Error(final String name, final Throwable e) {
	this(name, null, e);
    }

}
