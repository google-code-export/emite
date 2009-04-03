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
package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUI;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class StatusUIModule extends AbstractModule {

    public StatusUIModule() {
	super();
    }

    @Override
    public void onInstall() {
	register(Singleton.class, new Factory<StatusUI>(StatusUI.class) {
	    @Override
	    public StatusUI create() {
		final StatusUIPresenter presenter = new StatusUIPresenter($(Session.class), $(PresenceManager.class),
			$(Roster.class), $$(ChatManager.class), $$(RoomManager.class), $(SubscriptionUI.class),
			$(I18nTranslationService.class));
		final StatusUIPanel panel = new StatusUIPanel(presenter, $(I18nTranslationService.class));
		presenter.init(panel);
		return presenter;
	    }
	});

    }

}
