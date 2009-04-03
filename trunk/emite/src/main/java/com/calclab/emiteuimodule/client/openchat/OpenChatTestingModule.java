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
package com.calclab.emiteuimodule.client.openchat;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.ioc.module.AbstractModule;

public class OpenChatTestingModule extends AbstractModule {

    public OpenChatTestingModule() {
	super();
    }

    @Override
    public void onInstall() {
	final OpenChatTestingPresenter presenter = new OpenChatTestingPresenter($(ChatManager.class), $(StatusUI.class));
	final OpenChatTestingPanel panel = new OpenChatTestingPanel(presenter, $(StatusUI.class),
		$(I18nTranslationService.class));
	presenter.init(panel);
    }

}
