package com.calclab.emite.xep.privacylists.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;

/**
 * Will (i hope!) implement http://www.xmpp.org/extensions/xep-0016.html
 */
public class PrivacyListsManager {
    private final Session session;

    public PrivacyListsManager(final Session session) {
	this.session = session;
    }

    /**
     * Block incoming messages from other entity based on the entity's JID.
     * 
     * @see http://www.xmpp.org/extensions/xep-0016.html#protocol-message
     * 
     * @param listName
     *            is that necessary?
     * @param uri
     *            the other entity jid
     * @param order
     *            i din't read the spec... is that necessary?
     */
    public void blockUserBasedOnJID(final String listName, final XmppURI uri, final Integer order) {
	final IQ iq = new IQ(IQ.Type.set);
	final IPacket list = iq.addQuery("jabber:iq:privacy").addChild("list", null);
	list.With("name", listName);
	list.addChild("item", null).With("type", "jid").With("value", uri.getJID().toString()).With("action", "deny")
		.With("order", order.toString());

	session.sendIQ("privacyLists", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket parameter) {
		// here you can handle the response... i think in this case is
		// not needed
	    }
	});
    }
}
