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
import com.calclab.emite.im.client.Xmpp;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.dialog.QuickTipsHelper;
import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.room.RoomUIModule;
import com.calclab.emiteuimodule.client.sound.SoundManager;
import com.calclab.emiteuimodule.client.sound.SoundModule;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.status.StatusUIModule;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.NoScope;
import com.calclab.suco.client.scope.SingletonScope;

public class EmiteUIModule extends AbstractModule {

    public EmiteUIModule() {
	super();
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<I18nTranslationService>(I18nTranslationService.class) {
	    public I18nTranslationService create() {
		return new I18nTranslationServiceMocked();
	    }
	}, new Factory<QuickTipsHelper>(QuickTipsHelper.class) {
	    public QuickTipsHelper create() {
		return new QuickTipsHelper();
	    }
	});

	$(QuickTipsHelper.class);

	install(new StatusUIModule(), new SoundModule(), new RoomUIModule());

	// Only for UI test (comment during release):
	// install(new OpenChatTestingModule());

	register(SingletonScope.class, new Factory<EmiteUIFactory>(EmiteUIFactory.class) {
	    public EmiteUIFactory create() {
		return new EmiteUIFactory($(Xmpp.class), $(I18nTranslationService.class), $(StatusUI.class),
			$$(SoundManager.class));
	    }
	});

	register(NoScope.class, new Factory<EmiteUIDialog>(EmiteUIDialog.class) {
	    public EmiteUIDialog create() {
		return new EmiteUIDialog($(Connection.class), $(Session.class), $(ChatManager.class),
			$(EmiteUIFactory.class), $(RoomManager.class), $(XRoster.class), $(AvatarManager.class),
			$(StatusUI.class), $(RoomUIManager.class));
	    }
	});

    }
}
