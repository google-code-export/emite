package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.xmpp.stanzas.Presence;
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
            return icons.extendedAway();
        case MultiChatView.STATUS_AWAY:
            return icons.away();
        case MultiChatView.STATUS_MESSAGE:
            return icons.message();
        default:
            throw new IndexOutOfBoundsException("Xmpp status unknown");
        }
    }

    public static AbstractImagePrototype getStatusIcon(final Presence presence) {
        // Other icons to use
        // return icons.invisible();
        // return icons.away();
        // return icons.message();

        switch (presence.getType()) {
        case available:
            switch (presence.getShow()) {
            case available:
            case chat:
                return icons.online();
            case dnd:
                return icons.busy();
            case xa:
                return icons.extendedAway();
            default:
                Log.debug("Status unknown" + presence.getShow());
                return icons.online();
            }
        case unavailable:
            return icons.offline();
        default:
            throw new IndexOutOfBoundsException("Xmpp status unknown");
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
