package com.calclab.emite.xep.search.client;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Search information repositories on the Jabber network.
 * 
 * Implements XEP-0055: Jabber Search
 * 
 * @see http://xmpp.org/extensions/xep-0055.html
 */
public interface SearchManager {

    /**
     * Set the host service where send the requests. Usually you send all the
     * request to same service, so its common to setup once (probably from a
     * meta tag)
     * 
     * @param host
     *            the jid of the search service
     */
    public void setHost(XmppURI host);

    void requestSearchFields(Listener<List<String>> listener);

    /**
     * Request search fields TODO
     * 
     * (creo que deprecated porque no hace falta pasar los dos primeros
     * parámetros: el user siempre es el de la sessión y el to se puede
     * especificar una vez para todas)
     * 
     * @param from
     * @param to
     * @param onResult
     */
    @Deprecated
    void requestSearchFields(final XmppURI from, final XmppURI to, Listener<List<String>> onResult);

    /**
     * Perform search TODO
     * 
     * @param from
     * @param to
     * @param query
     * @param onResult
     */
    void search(final XmppURI from, final XmppURI to, HashMap<String, String> query, Listener<List<Item>> onResult);

}