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
package com.calclab.emite.examples.echo;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;

public class EchoModule {
    private static final Class<Echo> COMPONENT_ECHO = Echo.class;

    public static Echo getEcho(final Container container) {
	return container.getInstance(COMPONENT_ECHO);
    }

    public static void install(final Container container) {

	container.registerProvider(Echo.class, new Provider<Echo>() {
	    public Echo get() {
		return new Echo(container.getInstance(Emite.class));
	    }

	}, Scopes.SINGLETON_EAGER);

    }
}
