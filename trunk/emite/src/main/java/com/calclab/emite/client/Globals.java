package com.calclab.emite.client;

public interface Globals {
	String getDomain();

	String getJID();

	String getPassword();

	String getResourceName();

	String getUserName();

	void setDomain(String domain);

	void setJID(String jid);

	void setPassword(String password);

	void setResourceName(String resource);

	void setUserName(String userName);
}
