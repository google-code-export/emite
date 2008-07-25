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

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.services.gwt.GWTServicesModule;
import com.calclab.emite.client.xep.avatar.AvatarModule;
import com.calclab.emite.client.xep.chatstate.ChatStateModule;
import com.calclab.emite.client.xep.muc.MUCModule;
import com.calclab.emiteuimodule.client.room.RoomUIModule;
import com.calclab.emiteuimodule.client.sound.SoundManager;
import com.calclab.emiteuimodule.client.sound.SoundModule;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.status.StatusUIModule;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;
import com.calclab.suco.client.scopes.NoScope;
import com.calclab.suco.client.scopes.SingletonScope;

public class EmiteUIModule implements Module {

    public Class<? extends Module> getType() {
	return EmiteUIModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.add(new GWTServicesModule());
	builder.add(new EmiteModule(), new MUCModule(), new ChatStateModule(), new AvatarModule());

	builder.registerProvider(I18nTranslationService.class, new Provider<I18nTranslationService>() {
	    public I18nTranslationService get() {
		return new I18nTranslationServiceMocked();
	    }
	}, SingletonScope.class);

	builder.add(new StatusUIModule(), new SoundModule(), new RoomUIModule());

	// Only for UI test (comment during release):
	// builder.add(new OpenChatTestingModule());

	builder.registerProvider(EmiteUIFactory.class, new Provider<EmiteUIFactory>() {
	    public EmiteUIFactory get() {
		final StatusUI statusUI = builder.getInstance(StatusUI.class);
		final SoundManager soundManager = builder.getInstance(SoundManager.class);
		return new EmiteUIFactory(builder.getInstance(Xmpp.class), builder
			.getInstance(I18nTranslationService.class), statusUI, soundManager);
	    }
	}, SingletonScope.class);

	builder.registerProvider(EmiteUIDialog.class, new Provider<EmiteUIDialog>() {
	    public EmiteUIDialog get() {
		final Xmpp xmpp = builder.getInstance(Xmpp.class);
		final StatusUI statusUI = builder.getInstance(StatusUI.class);
		final EmiteUIFactory factory = builder.getInstance(EmiteUIFactory.class);
		return new EmiteUIDialog(xmpp, factory, statusUI);
	    }

	}, NoScope.class);

    }
}
