package com.calclab.emiteuiplugin.client.roster;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.users.ChatUserUI;
import com.calclab.emiteuiplugin.client.users.UserGridMenuItemList;

public interface RosterView {

    public void addRosterItem(final ChatUserUI user, final UserGridMenuItemList menuItemList);

    public void clearRoster();

    public void confirmSusbscriptionRequest(final Presence presence);

    public void informRosterItemRemoved(final ChatUserUI user);

    public void removeRosterItem(final ChatUserUI user);

    public void showMessageAboutUnsuscription(final XmppURI userUnsubscribed);

    public void updateRosterItem(final ChatUserUI user, final UserGridMenuItemList menuItemList);

}