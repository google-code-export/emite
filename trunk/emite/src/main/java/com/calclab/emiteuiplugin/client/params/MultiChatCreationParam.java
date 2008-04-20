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
package com.calclab.emiteuiplugin.client.params;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emiteuiplugin.client.UserChatOptions;

public class MultiChatCreationParam {

    private final BoshOptions boshOptions;
    private final UserChatOptions userChatOptions;
    private final I18nTranslationService i18nService;

    public MultiChatCreationParam(final BoshOptions boshOptions, final I18nTranslationService i18nService,
            final UserChatOptions userChatOptions) {
        this.boshOptions = boshOptions;
        this.i18nService = i18nService;
        this.userChatOptions = userChatOptions;
    }

    public BoshOptions getBoshOptions() {
        return boshOptions;
    }

    public I18nTranslationService getI18nService() {
        return i18nService;
    }

    public UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

}
