/*
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
