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
package com.calclab.emite.client.modular;

import java.util.HashMap;

/**
 * A container with module installation support
 */
public class ModuleBuilder extends DelegatedContainer {
    private HashMap<Class<?>, Module> modules;

    public ModuleBuilder() {
	this(new HashContainer());
	this.modules = new HashMap<Class<?>, Module>();
    }

    public ModuleBuilder(final Container delegate) {
	super(delegate);
    }

    /**
     * load the modules list into the container
     * 
     * @param module
     *                list
     */
    public void add(final Module... toAddModules) {
	for (final Module m : toAddModules) {
	    loadIfNeeded(m);
	}
    }

    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider, final Scope scope) {
	return registerProvider(componentKey, provider, scope, null);
    }

    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider, final Scope scope,
	    final Context<?> context) {
	final Provider<T> scoped = scope.scope(componentKey, provider);
	super.registerProvider(componentKey, scoped);
	if (context != null) {
	    context.register(this, provider);
	}
	return scoped;
    }

    private void loadIfNeeded(final Module m) {
	final Class<?> type = m.getType();
	final Module oldModule = modules.get(type);
	if (oldModule == null) {
	    modules.put(type, m);
	    m.onLoad(this);
	}
    }
}
