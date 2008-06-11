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
package com.calclab.emite.client;

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.im.InstantMessagingModule;
import com.calclab.emite.client.xmpp.XMPPModule;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;
import com.calclab.suco.client.scopes.SingletonScope;

public class EmiteModule implements Module {

    public static Xmpp getXmpp(final Container container) {
	return container.getInstance(Xmpp.class);
    }

    public Class<? extends Module> getType() {
	return EmiteModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.add(new CoreModule(), new XMPPModule(), new InstantMessagingModule());
	builder.registerProvider(Xmpp.class, new Provider<Xmpp>() {
	    public Xmpp get() {
		return new Xmpp(builder);
	    }

	}, SingletonScope.class);
    }

}
