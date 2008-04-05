package com.calclab.emite.client.components;

import java.util.HashMap;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

@SuppressWarnings("serial")
public class HashGlobals extends HashMap<String, Object> implements Globals {
	private static final String DOMAIN = "domain";
	private static final String PASSWORD = "password";
	private static final String RESOURCE = "resource";
	private static final String URI = "xmpp:uri";
	private static final String USER_NAME = "userName";

	public String getDomain() {
		return (String) get(DOMAIN);
	}

	public XmppURI getOwnURI() {
		return (XmppURI) get(URI);
	}

	public String getPassword() {
		return (String) get(PASSWORD);
	}

	public String getResourceName() {
		return (String) get(RESOURCE);
	}

	public String getUserName() {
		return (String) get(USER_NAME);
	}

	public void setDomain(final String domain) {
		put(DOMAIN, domain);
	}

	public void setOwnURI(final XmppURI jid) {
		put(URI, jid);
	}

	public void setPassword(final String password) {
		put(PASSWORD, password);
	}

	public void setResourceName(final String resource) {
		put(RESOURCE, resource);

	}

	public void setUserName(final String userName) {
		put(USER_NAME, userName);

	}

}
