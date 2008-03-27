package com.calclab.emite.client;

import java.util.HashMap;

@SuppressWarnings("serial")
public class HashGlobals extends HashMap<String, String> implements Globals {
	private static final String DOMAIN = "domain";
	private static final String JID = "jid";
	private static final String PASSWORD = "password";
	private static final String RESOURCE = "resource";
	private static final String USER_NAME = "userName";

	public String getDomain() {
		return get(DOMAIN);
	}

	public String getJID() {
		return get(JID);
	}

	public String getPassword() {
		return get(PASSWORD);
	}

	public String getResourceName() {
		return get(RESOURCE);
	}

	public String getUserName() {
		return get(USER_NAME);
	}

	public void setDomain(final String domain) {
		put(DOMAIN, domain);
	}

	public void setJID(final String jid) {
		put(JID, jid);
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
