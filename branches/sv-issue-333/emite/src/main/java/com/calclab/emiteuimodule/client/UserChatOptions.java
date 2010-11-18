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
package com.calclab.emiteuimodule.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class UserChatOptions {

    private String color;
    private SubscriptionMode subscriptionMode;
    private final XmppURI userJid;
    private final String userPassword;
    private final String resource;
    private boolean unavailableRosterItemsVisible;

    public UserChatOptions(final String userJid, final String userPassword, final String resource, final String color,
	    final SubscriptionMode subscriptionMode, final boolean unavailableRosterItemsVisible) {
	this.userJid = XmppURI.uri(userJid);
	this.userPassword = userPassword;
	this.resource = resource;
	this.color = color;
	this.subscriptionMode = subscriptionMode;
	this.unavailableRosterItemsVisible = unavailableRosterItemsVisible;
    }

    public String getColor() {
	return color;
    }

    public String getResource() {
	return resource;
    }

    public SubscriptionMode getSubscriptionMode() {
	return subscriptionMode;
    }

    public XmppURI getUserJid() {
	return userJid;
    }

    public String getUserPassword() {
	return userPassword;
    }

    public boolean isUnavailableRosterItemsVisible() {
	return unavailableRosterItemsVisible;
    }

    public void setColor(final String color) {
	this.color = color;
    }

    public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
	this.subscriptionMode = subscriptionMode;
    }

    public void setUnavailableRosterItemsVisible(final boolean unavailableRosterItemsVisible) {
	this.unavailableRosterItemsVisible = unavailableRosterItemsVisible;
    }

}
