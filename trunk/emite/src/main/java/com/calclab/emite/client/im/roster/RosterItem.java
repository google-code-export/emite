package com.calclab.emite.client.im.roster;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RosterItem {
    private final ArrayList<String> groups;
    private final String name;
    private final String subscription;
    private final XmppURI uri;

    public RosterItem(final XmppURI uri, final String subscription, final String name) {
	this.uri = uri;
	this.subscription = subscription;
	this.name = name;
	this.groups = new ArrayList<String>();
    }

    public List<String> getGroups() {
	return groups;
    }

    public String getName() {
	return name;
    }

    public String getSubscription() {
	return subscription;
    }

    public XmppURI getXmppURI() {
	return uri;
    }

}
