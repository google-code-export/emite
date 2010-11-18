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
package com.calclab.emiteuimodule.client;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;
import org.ourproject.kune.platf.client.i18n.I18nTranslationServiceMocked;
import org.ourproject.kune.platf.client.ui.QuickTipsHelper;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.chatstate.client.StateManager;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.room.RoomUIModule;
import com.calclab.emiteuimodule.client.sound.SoundManager;
import com.calclab.emiteuimodule.client.sound.SoundModule;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.status.StatusUIModule;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUI;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUIPanel;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUIPresenter;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class EmiteUIModule extends AbstractModule {

    public EmiteUIModule() {
	super();
    }

    @Override
    public void onInstall() {

	if (!container.hasProvider(I18nTranslationService.class)) {
	    register(Singleton.class, new Factory<I18nTranslationService>(I18nTranslationService.class) {
		@Override
		public I18nTranslationService create() {
		    return new I18nTranslationServiceMocked();
		}
	    });
	}

	register(SessionComponent.class, new Factory<SubscriptionUI>(SubscriptionUI.class) {
	    @Override
	    public SubscriptionUI create() {
		final SubscriptionUIPresenter presenter = new SubscriptionUIPresenter($(SubscriptionManager.class));
		final SubscriptionUIPanel panel = new SubscriptionUIPanel(presenter, $(I18nTranslationService.class));
		presenter.init(panel);
		return presenter;
	    }
	});

	if (!container.hasProvider(QuickTipsHelper.class)) {
	    register(Singleton.class, new Factory<QuickTipsHelper>(QuickTipsHelper.class) {
		@Override
		public QuickTipsHelper create() {
		    return new QuickTipsHelper();
		}
	    });
	}

	$(QuickTipsHelper.class);

	install(new StatusUIModule(), new SoundModule(), new RoomUIModule());

	// Only for UI test (comment during release):
	// install(new OpenChatTestingModule());

	register(Singleton.class, new Factory<EmiteUIFactory>(EmiteUIFactory.class) {
	    @Override
	    public EmiteUIFactory create() {
		return new EmiteUIFactory($(ChatManager.class), $(Roster.class), $(SubscriptionManager.class),
			$(I18nTranslationService.class), $(StatusUI.class), $$(SoundManager.class),
			$(RoomManager.class), $(StateManager.class), $(AvatarManager.class));
	    }
	});

	register(Singleton.class, new Factory<EmiteUIDialog>(EmiteUIDialog.class) {
	    @Override
	    public EmiteUIDialog create() {
		return new EmiteUIDialog($(Connection.class), $(Session.class), $(ChatManager.class),
			$(EmiteUIFactory.class), $(RoomManager.class), $(AvatarManager.class), $(StatusUI.class),
			$(RoomUIManager.class), $(Roster.class));
	    }
	});

    }
}
