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
package com.calclab.emiteuimodule.client;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.chatstate.client.StateManager;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.dialog.QuickTipsHelper;
import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.room.RoomUIModule;
import com.calclab.emiteuimodule.client.sound.SoundManager;
import com.calclab.emiteuimodule.client.sound.SoundModule;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.status.StatusUIModule;
import com.calclab.suco.client.ioc.decorator.NoDecoration;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class EmiteUIModule extends AbstractModule {

    public EmiteUIModule() {
	super();
    }

    @Override
    public void onLoad() {
	register(Singleton.class, new Factory<I18nTranslationService>(I18nTranslationService.class) {
	    @Override
	    public I18nTranslationService create() {
		return new I18nTranslationServiceMocked();
	    }
	}, new Factory<QuickTipsHelper>(QuickTipsHelper.class) {
	    @Override
	    public QuickTipsHelper create() {
		return new QuickTipsHelper();
	    }
	});

	$(QuickTipsHelper.class);

	install(new StatusUIModule(), new SoundModule(), new RoomUIModule());

	// Only for UI test (comment during release):
	// install(new OpenChatTestingModule());

	register(Singleton.class, new Factory<EmiteUIFactory>(EmiteUIFactory.class) {
	    @Override
	    public EmiteUIFactory create() {
		return new EmiteUIFactory($(ChatManager.class), $(XRoster.class), $(XRosterManager.class),
			$(I18nTranslationService.class), $(StatusUI.class), $$(SoundManager.class),
			$(RoomManager.class), $(StateManager.class), $(AvatarManager.class));
	    }
	});

	register(NoDecoration.class, new Factory<EmiteUIDialog>(EmiteUIDialog.class) {
	    @Override
	    public EmiteUIDialog create() {
		return new EmiteUIDialog($(Connection.class), $(Session.class), $(ChatManager.class),
			$(EmiteUIFactory.class), $(RoomManager.class), $(XRoster.class), $(AvatarManager.class),
			$(StatusUI.class), $(RoomUIManager.class));
	    }
	});

    }
}
