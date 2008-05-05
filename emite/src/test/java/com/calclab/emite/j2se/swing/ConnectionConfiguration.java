package com.calclab.emite.j2se.swing;

public class ConnectionConfiguration {
    public final String name;
    public final String httpBase;
    public final String domain;
    public final String userName;
    public final String password;

    public ConnectionConfiguration(final String name, final String httpBase, final String domain,
	    final String userName, final String password) {
	this.name = name;
	this.httpBase = httpBase;
	this.domain = domain;
	this.userName = userName;
	this.password = password;
    }

    @Override
    public String toString() {
	return name;
    }
}
