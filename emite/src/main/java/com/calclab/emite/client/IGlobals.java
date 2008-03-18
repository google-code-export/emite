package com.calclab.emite.client;

public interface IGlobals {
	String getDomain();

	String getJID();

	String getPassword();

	String getUserName();

	void setPassword(String password);

	void setUserName(String userName);
}
