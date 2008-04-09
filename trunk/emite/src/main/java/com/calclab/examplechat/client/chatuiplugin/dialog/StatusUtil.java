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
package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.examplechat.client.chatuiplugin.utils.ChatIcons;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class StatusUtil {

    private static final ChatIcons icons = ChatIcons.App.getInstance();

    @Deprecated
    public static AbstractImagePrototype getStatusIcon(final int status) {
        switch (status) {
        case MultiChatView.STATUS_ONLINE:
            return icons.online();
        case MultiChatView.STATUS_OFFLINE:
            return icons.offline();
        case MultiChatView.STATUS_BUSY:
            return icons.busy();
        case MultiChatView.STATUS_INVISIBLE:
            return icons.invisible();
        case MultiChatView.STATUS_XA:
            return icons.xa();
        case MultiChatView.STATUS_AWAY:
            return icons.away();
        case MultiChatView.STATUS_MESSAGE:
            return icons.message();
        default:
            throw new IndexOutOfBoundsException("Xmpp status unknown");
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
                        Log.debug("Status unknown, show: " + presence.getShow());
                        return icons.question();
                    }
                } else {
                    return icons.question();
                }
            case unavailable:
                return icons.offline();
            case unsubscribed:
                return icons.notAuthorized();
            case subscribed:
                return icons.question();
            default:
                Log.error("Programatic error, status: " + statusType);
                return icons.question();
            }
        case from:
            return icons.notAuthorized();
        case none:
            return icons.notAuthorized();
        default:
            Log.error("Programatic error, subscription: " + subscription);
            return icons.question();
        }
    }

    @Deprecated
    public static String getStatusText(final I18nTranslationService i18n, final int status) {
        String textLabel;

        switch (status) {
        case MultiChatView.STATUS_ONLINE:
            textLabel = i18n.t("online");
            break;
        case MultiChatView.STATUS_OFFLINE:
            textLabel = i18n.t("offline");
            break;
        case MultiChatView.STATUS_BUSY:
            textLabel = i18n.t("busy");
            break;
        case MultiChatView.STATUS_INVISIBLE:
            textLabel = i18n.t("invisible");
            break;
        case MultiChatView.STATUS_XA:
            textLabel = i18n.t("extended away");
            break;
        case MultiChatView.STATUS_AWAY:
            textLabel = i18n.t("away");
            break;
        default:
            throw new IndexOutOfBoundsException("Xmpp status unknown");
        }
        return textLabel;
    }

    public static String getStatusIconAndText(final I18nTranslationService i18n, final int status) {
        return getStatusIcon(status).getHTML() + "&nbsp;" + getStatusText(i18n, status);
    }

}
