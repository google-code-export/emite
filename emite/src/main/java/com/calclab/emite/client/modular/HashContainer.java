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
 * A class that implements the Container interface using a HashMap
 * 
 * @author dani
 */
public class HashContainer implements Container {
    private final HashMap<Class<?>, Provider<?>> components;

    public HashContainer() {
	this.components = new HashMap<Class<?>, Provider<?>>();
    }

    public <T> T getInstance(final Class<T> componentKey) {
	return getProvider(componentKey).get();
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProvider(final Class<T> componentKey) {
	final Provider<T> provider = (Provider<T>) components.get(componentKey);
	if (provider == null) {
	    throw new RuntimeException("component not registered: " + componentKey);
	}
	return provider;
    }

    public <T> T registerSingletonInstance(final Class<T> componentType, final T component) {
	registerProvider(componentType, new Provider<T>() {
	    public T get() {
		return component;
	    }
	});
	return component;
    }

    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider) {
	components.put(componentKey, provider);
	return provider;
    }
}
