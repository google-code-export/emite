package com.calclab.emite.client.components;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface Globals {
	String getDomain();

	XmppURI getOwnURI();

	String getPassword();

	String getResourceName();

	String getUserName();

	void setDomain(String domain);

	void setOwnURI(XmppURI uri);

	void setPassword(String password);

	void setResourceName(String resource);

	void setUserName(String userName);
}
