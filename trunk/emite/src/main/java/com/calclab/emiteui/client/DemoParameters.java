package com.calclab.emiteui.client;

import com.google.gwt.user.client.DOM;

public class DemoParameters {
    private static final String GWT_PROPERTY_JID = "gwt_property_jid";
    private static final String GWT_PROPERTY_PASSWD = "gwt_property_passwd";
    private static final String GWT_PROPERTY_HTTPBASE = "gwt_property_httpbase";
    private static final String GWT_PROPERTY_ROOMHOST = "gwt_property_roomhost";
    private static final String GWT_PROPERTY_INFOHTML = "gwt_property_infohtml";
    private static final String GWT_PROPERTY_RELEASE = "gwt_property_release";

    public String getHttpBase() {
	return getGwtMetaProperty(GWT_PROPERTY_HTTPBASE, null);
    }

    public String getInfo(final String defaultVal) {
	return getGwtMetaProperty(GWT_PROPERTY_INFOHTML, defaultVal);
    }

    public String getJID() {
	return getGwtMetaProperty(GWT_PROPERTY_JID, null);
    }

    public String getPassword() {
	return getGwtMetaProperty(GWT_PROPERTY_PASSWD, null);
    }

    public String getRelease() {
	return getGwtMetaProperty(GWT_PROPERTY_RELEASE, null);
    }

    public String getRoomHost() {
	return getGwtMetaProperty(GWT_PROPERTY_ROOMHOST, null);
    }

    private String getGwtMetaProperty(final String property, final String defaultVal) {
	final String value = DOM.getElementProperty(DOM.getElementById(property), "content");
	return value != null ? value : defaultVal;
    }
}
