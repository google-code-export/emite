package com.calclab.examplechat.client.chatuiplugin.modeltointegratewithemite;

/**
 * 
 * http://www.xmpp.org/drafts/attic/draft-saintandre-xmpp-uri-00.html
 * 
 * <code>XMPP-URI      = "xmpp:" jid [ "/" resource]</code>
 * 
 */

public class XmppUriSimple {
    XmppJid jid;
    String resource;

    /**
     * 
     * @param jid
     * @param resource
     *                <code> resource      = *( unreserved / escaped )
                reserved      = ";" / "/" / "?" / ":" / "@" / "&" / "=" / "+" /
                "$" / "," / "[" / "]" </code>
     * 
     */
    public XmppUriSimple(final XmppJid jid, final String resource) {
        this.jid = jid;
        this.resource = resource;
    }

    public XmppUriSimple(final String xmppUri) {
        String[] splitted1 = xmppUri.split("\\:");
        if (splitted1.length != 2 || !splitted1[0].equals("xmpp")) {
            throwException(xmppUri);
        }
        String[] splitted2 = splitted1[1].split("/");
        if (splitted2.length != 2 || !(splitted2[1].length() > 0)) {
            throwException(xmppUri);
        }
        jid = new XmppJid(splitted2[0]);
        this.resource = splitted2[1];
    }

    private void throwException(final String xmppUri) {
        throw new RuntimeException("Wrong XmppUri format" + xmppUri);
    }

    @Override
    public String toString() {
        return "xmpp:" + jid + "/" + resource;
    }

    public XmppJid getJid() {
        return jid;
    }

    public String getResource() {
        return resource;
    }
}
