package com.calclab.uimite.client.config;

import com.calclab.emite.client.Xmpp;
import com.google.gwt.user.client.DOM;

public class Configurator {
    private static final String HTTP_BASE_PROPERTY = "emite_bosh_httpbase";

    public Configurator(final Xmpp xmpp) {
	final String httpBase = getMetaProperty(HTTP_BASE_PROPERTY);
	if (httpBase != null)
	    xmpp.setHttpBase(httpBase);
    }

    public String getMetaProperty(final String property) {
	return DOM.getElementProperty(DOM.getElementById(property), "content");
    }

}
