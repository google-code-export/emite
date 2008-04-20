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

import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.widgets.Panel;

public class MultiChatPanelInfoTab extends Panel {
    private final Label infoLabel;
    private final I18nTranslationService i18n;

    public MultiChatPanelInfoTab(final I18nTranslationService i18n) {
        this.i18n = i18n;
        setTitle(i18n.t("Info"));
        setClosable(false);
        infoLabel = new Label();
        add(infoLabel);
        setPaddings(7);
    }

    public void setOfflineInfo() {
        infoLabel.setText(i18n.t("To start a chat you need to be 'online'."));
        if (isRendered()) {
            doLayout();
        }
    }

    public void setOnlineInfo() {
        infoLabel.setText(i18n.t("To start a chat, select a buddy or join to a chat room. "
                + "If you don't have buddies you can add them. "));
        if (isRendered()) {
            doLayout();
        }
    }
}
