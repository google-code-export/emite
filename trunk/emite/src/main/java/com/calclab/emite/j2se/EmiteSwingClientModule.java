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
package com.calclab.emite.j2se;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.xep.disco.DiscoveryModule;
import com.calclab.emite.client.xep.muc.MUCModule;
import com.calclab.emite.j2se.services.J2SEServicesModule;
import com.calclab.emite.j2se.swing.SwingClient;
import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.modules.Module;
import com.calclab.modular.client.modules.ModuleBuilder;
import com.calclab.modular.client.scopes.SingletonScope;

public class EmiteSwingClientModule implements Module {

    public static void main(final String args[]) {
	final ModuleBuilder container = new ModuleBuilder();
	container.add(new J2SEServicesModule(), new EmiteModule(), new MUCModule(), new DiscoveryModule());
	container.add(new EmiteSwingClientModule());
	container.getInstance(SwingClient.class).start();
    }

    public Class<?> getType() {
	return EmiteSwingClientModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	final Xmpp xmpp = builder.getInstance(Xmpp.class);

	builder.registerProvider(SwingClient.class, new Provider<SwingClient>() {
	    public SwingClient get() {
		return new SwingClient(xmpp);
	    }
	}, SingletonScope.class);
    }
}
