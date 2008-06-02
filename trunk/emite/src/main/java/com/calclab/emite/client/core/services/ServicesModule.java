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
package com.calclab.emite.client.core.services;

import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleBuilder;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.scopes.SingletonScope;

public abstract class ServicesModule implements Module {
    private static final Class<Services> COMPONENT_SERVICES = Services.class;

    public static Services getServices(final Container container) {
	return container.getInstance(COMPONENT_SERVICES);
    }

    public static void setServices(final ModuleBuilder builder, final Services services) {
	builder.registerProvider(Services.class, new Provider<Services>() {
	    public Services get() {
		return services;
	    }
	}, SingletonScope.class);
    }

    public Class<? extends Module> getType() {
	return ServicesModule.class;
    }

}
