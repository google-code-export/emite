/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteui.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.DOM;

public class DemoParameters {
    private static final String GWT_PROPERTY_JID = "emite:user:jid";
    private static final String GWT_PROPERTY_PASSWD = "emite:user:password";
    private static final String GWT_PROPERTY_HTTPBASE = "emite:bosh:httpbase";
    private static final String GWT_PROPERTY_HOST = "emite:bosh:host";
    private static final String GWT_PROPERTY_ROOMHOST = "emite:muc:host";
    private static final String GWT_PROPERTY_INFOHTML = "emite:demo:info";
    private static final String GWT_PROPERTY_RELEASE = "emite:demo:version";

    public String getHost() {
        return getGwtMetaProperty(GWT_PROPERTY_HOST, null);
    }

    public String getHttpBase() {
        return getGwtMetaProperty(GWT_PROPERTY_HTTPBASE, null);
    }

    public String getInfo(final String defaultVal) {
        return getGwtMetaProperty(GWT_PROPERTY_INFOHTML, null);
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

    private String getGwtMetaProperty(final String property, final String optionalValue) {
        String value = DOM.getElementProperty(DOM.getElementById(property), "content");
        if (value == null && optionalValue == null) {
            throw new RuntimeException("Property: " + property + " not found!");
        } else {
            value = value != null ? value : optionalValue;
            Log.debug("Property => " + property + ": " + value);
            return value;
        }
    }
}
