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
package com.calclab.emiteuimodule.client.utils;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ChatUIUtils {

    public static AbstractImagePrototype getIcon(final ChatIconDescriptor iconDescriptor) {
        final ChatIcons icons = ChatIcons.App.getInstance();
        switch (iconDescriptor) {
        case available:
        case chat:
            return icons.online();
        case away:
            return icons.away();
        case dnd:
            return icons.busy();
        case newmessage:
            return icons.newMessage();
        case offline:
            return icons.offline();
        case unknown:
            return icons.question();
        case xa:
            return icons.xa();
        case chatsmall:
            return icons.chatSmall();
        case chatnewmessagesmall:
            return icons.chatNewMessageSmall();
        case roomsmall:
            return icons.roomSmall();
        case roomnewmessagesmall:
            return icons.roomNewMessageSmall();
        default:
            return null;
        }
    }

    public static AbstractImagePrototype getOwnStatusIcon(final OwnStatus ownStatus) {
        final ChatIcons icons = ChatIcons.App.getInstance();
        switch (ownStatus) {
        case online:
        case onlinecustom:
            return icons.online();
        case busy:
        case busycustom:
            return icons.busy();
        case offline:
            return icons.offline();
        default:
            Log.error("Code error in OwnPresence getStatusIcon");
            return icons.offline();
        }
    }

    public static String getOwnStatusIconAndText(final I18nTranslationService i18n, final OwnStatus ownStatus) {
        return getOwnStatusIcon(ownStatus).getHTML() + "&nbsp;" + getOwnStatusText(i18n, ownStatus);
    }

    private static String getOwnStatusText(final I18nTranslationService i18n, final OwnStatus ownStatus) {
        String textLabel;

        switch (ownStatus) {
        case online:
            textLabel = i18n.t("online");
            break;
        case offline:
            textLabel = i18n.t("offline");
            break;
        case busy:
            textLabel = i18n.t("busy");
            break;
        case busycustom:
            textLabel = i18n.t("busy with custom message");
            break;
        case onlinecustom:
            textLabel = i18n.t("online with custom message");
            break;
        default:
            return null;
        }
        return textLabel;
    }

}
