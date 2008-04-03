package com.calclab.emite.client.xmpp.stanzas;

/**
 * 
 * Syntax of a xmpp jid (somebody@example.com) :
 * 
 * <code>jid           = [ node "@" ] host</code>
 * 
 */
public class XmppJID {
	public static XmppJID parseJID(final String jid) {
		final String[] splitted = jid.split("@");
		if (splitted.length != 2 || !(splitted[0].length() > 0) || !(splitted[1].length() > 0)) {
			throw new RuntimeException("Wrong XmppJid format" + jid);
		}
		final String node = splitted[0];
		final String host = splitted[1];
		return new XmppJID(node, host);
	}

	private final String host;

	private final String node;

	private final String repr;

	/**
	 * Xmmp JID constructor
	 * 
	 * @param node *(
	 *            alphanum / escaped / "-" / "_" / "." / "!" / "~" / "*" / "(" /
	 *            ")" )</code>
	 * @param host
	 *            <code>hostname / IPv4address / IPv6reference</code>
	 */
	public XmppJID(final String node, final String host) {
		this.node = node;
		this.host = host;
		this.repr = node + "@" + host;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		final XmppJID other = (XmppJID) obj;
		return repr.equals(other.repr);
	}

	public String getHost() {
		return host;
	}

	public String getNode() {
		return node;
	}

	@Override
	public int hashCode() {
		return repr.hashCode();
	}

	@Override
	public String toString() {
		return repr;
	}

}
