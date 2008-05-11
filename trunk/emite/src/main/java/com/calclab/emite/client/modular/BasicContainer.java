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

import com.calclab.emite.client.modular.Container;

@SuppressWarnings("serial")
public class BasicContainer extends HashMap<Class<?>, Object> implements Container {

    public <T> T get(final Class<T> componentType) {
	final T component = (T) super.get(componentType);
	if (component == null) {
	    throw new RuntimeException("component not registered: " + componentType);
	}
	return component;
    }

    public void install(final Module... modules) {
	for (final Module m : modules) {
	    m.load(this);
	}
    }

    public <T> T register(final Class<T> componentType, final T component) {
	super.put(componentType, component);
	return component;
    }
}
