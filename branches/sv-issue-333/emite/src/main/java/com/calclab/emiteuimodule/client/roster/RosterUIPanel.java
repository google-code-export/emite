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
package com.calclab.emiteuimodule.client.roster;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.DragGridConfiguration;
import com.calclab.emiteuimodule.client.users.DropGridConfiguration;
import com.calclab.emiteuimodule.client.users.UserGridListener;
import com.calclab.emiteuimodule.client.users.UserGridMenu;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;
import com.calclab.emiteuimodule.client.users.UserGridPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;

public class RosterUIPanel extends UserGridPanel implements RosterUIView {
    private final I18nTranslationService i18n;

    public RosterUIPanel(final I18nTranslationService i18n, final RosterUIPresenter presenter) {
	super(i18n.t("No buddies at this moment"), new DragGridConfiguration(USER_GROUP_DD, i18n
		.t("Drop in the chat area to start a chat.")
		+ "<br/>" + i18n.t("Drop into a room to invite the user to join the chat room")),
		new UserGridListener() {
		    public void onDoubleClick(final XmppURI userJid) {
			presenter.openChat(userJid);
		    }
		});
	this.i18n = i18n;
    }

    public void addRosterItem(final ChatUserUI user, final UserGridMenuItemList menuItemList) {
	final UserGridMenu menu = new UserGridMenu();
	menu.setMenuItemList(menuItemList);
	super.addUser(user, menu);
    }

    public void clearRoster() {
	super.removeAllUsers();
    }

    @Override
    public void confDropInPanel(final Panel panel, final DropGridConfiguration dropGridConfiguration) {
	super.confDropInPanel(panel, dropGridConfiguration);
    }

    public void informRosterItemRemoved(final ChatUserUI user) {
	// not used currently
	MessageBox.alert(i18n.t("[%s] has removed you from his/her buddies list", user.getURI().toString()));
    }

    public void removeRosterItem(final ChatUserUI user) {
	super.removeUser(user);
    }

    public void showMessageAboutUnsuscription(final XmppURI userUnsubscribed) {
	MessageBox.alert(i18n.t("[%s] has decided to stop to show you his/her status", userUnsubscribed.toString()));
    }

    public void updateRosterItem(final ChatUserUI user, final UserGridMenuItemList menuItemList) {
	final UserGridMenu menu = new UserGridMenu();
	menu.setMenuItemList(menuItemList);
	super.updateRosterItem(user, menu);
    }
}
