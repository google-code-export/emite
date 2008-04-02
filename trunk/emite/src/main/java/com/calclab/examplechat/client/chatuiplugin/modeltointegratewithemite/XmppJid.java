package com.calclab.examplechat.client.chatuiplugin.modeltointegratewithemite;

/**
 * 
 * Syntax of a xmpp jid (somebody@example.com) :
 * 
 * <code>jid           = [ node "@" ] host</code>
 * 
 */
public class XmppJid {
    String node;
    String host;

    public XmppJid() {
        node = "";
        host = "";
    }

    /**
     * Xmmp JID constructor
     * 
     * @param node *(
     *                alphanum / escaped / "-" / "_" / "." / "!" / "~" / "*" /
     *                "(" / ")" )</code>
     * @param host
     *                <code>hostname / IPv4address / IPv6reference</code>
     */
    public XmppJid(final String node, final String host) {
        this.node = node;
        this.host = host;
    }

    public XmppJid(final String jid) {
        String[] splitted = jid.split("@");
        if (splitted.length != 2 || !(splitted[0].length() > 0) || !(splitted[1].length() > 0)) {
            throw new RuntimeException("Wrong XmppJid format" + jid);
        }
        node = splitted[0];
        host = splitted[1];
    }

    @Override
    public String toString() {
        return node + "@" + "host";
    }

    public String getNode() {
        return node;
    }

    public String getHost() {
        return host;
    }
}
