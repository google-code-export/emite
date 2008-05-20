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
package com.calclab.emiteui.client;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emiteuiplugin.client.EmiteUIFactory;
import com.calclab.emiteuiplugin.client.EmiteDialog;

public class EmiteUIModule implements Module {

    public static void loadWithDependencies(final ModuleContainer container) {
	container.add(new EmiteUIModule());
    }

    public Class<? extends Module> getType() {
	return EmiteUIModule.class;
    }

    public void onLoad(final Container container) {
	final Xmpp xmpp = container.getInstance(Xmpp.class);

	final I18nTranslationService i18n = container.registerSingletonInstance(I18nTranslationService.class,
		new I18nTranslationServiceMocked());

	final EmiteUIFactory factory = container.registerSingletonInstance(EmiteUIFactory.class, new EmiteUIFactory(
		xmpp, i18n));

	container.registerProvider(EmiteDialog.class, new Provider<EmiteDialog>() {
	    public EmiteDialog get() {
		return new EmiteDialog(xmpp, factory);
	    }

	});
    }
}
