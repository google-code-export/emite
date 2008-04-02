package com.calclab.emite.client.core.services;

public interface Globals {
	String getDomain();

	String getPassword();

	String getResourceName();

	String getUserName();

	String getXmppURI();

	void setDomain(String domain);

	void setPassword(String password);

	void setResourceName(String resource);

	void setUserName(String userName);

	void setXmppURI(String uri);
}
