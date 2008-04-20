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
package com.calclab.emiteuiplugin.client.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence.OwnStatus;
import com.calclab.emiteuiplugin.client.utils.ChatIcons;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class StatusUtil {

    private static final ChatIcons icons = ChatIcons.App.getInstance();

    public static AbstractImagePrototype getStatusIcon(final OwnStatus ownStatus) {
        ChatIcons icons = ChatIcons.App.getInstance();
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

    public static AbstractImagePrototype getStatusIcon(final Subscription subscription, final Presence presence) {
        Type statusType;
        if (presence == null) {
            statusType = Presence.Type.unavailable;
        } else {
            statusType = presence.getType();
        }

        switch (subscription) {
        case both:
        case to:
            switch (statusType) {
            case available:
                if (presence.getShow() != null) {
                    switch (presence.getShow()) {
                    case available:
                    case chat:
                        return icons.online();
                    case dnd:
                        return icons.busy();
                    case xa:
                        return icons.xa();
                    case away:
                        return icons.away();
                    default:
                        Log.info("Status unknown, show: " + presence.getShow());
                        return icons.question();
                    }
                } else {
                    /*
                     * 2.2.2.1. Show
                     * 
                     * If no <show/> element is provided, the entity is assumed
                     * to be online and available.
                     * 
                     */
                    return icons.online();
                }
            case unavailable:
                return icons.offline();
            case unsubscribed:
                return icons.notAuthorized();
            case subscribed:
                return icons.question();
            default:
                /**
                 * 2.2.1. Types of Presence
                 * 
                 * The 'type' attribute of a presence stanza is OPTIONAL. A
                 * presence stanza that does not possess a 'type' attribute is
                 * used to signal to the server that the sender is online and
                 * available for communication. If included, the 'type'
                 * attribute specifies a lack of availability, a request to
                 * manage a subscription to another entity's presence, a request
                 * for another entity's current presence, or an error related to
                 * a previously-sent presence stanza.
                 */
                return icons.online();
            }
        case from:
            return icons.question();
        case none:
            return icons.question();
        default:
            Log.error("Programatic error, subscription: " + subscription);
            return icons.question();
        }
    }

    public static String getStatusIconAndText(final I18nTranslationService i18n, final OwnStatus ownStatus) {
        return getStatusIcon(ownStatus).getHTML() + "&nbsp;" + getStatusText(i18n, ownStatus);
    }

    public static String getStatusText(final I18nTranslationService i18n, final OwnStatus ownStatus) {
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
            Log.error("Code error in OwnPresence getStatusText");
            return null;
        }
        return textLabel;
    }

}
