/**
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
