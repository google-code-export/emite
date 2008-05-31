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

import com.allen_sauer.gwt.log.client.Log;

public class Scopes {
    public static final Scope SINGLETON = new Scope() {
	public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	    return new Provider<T>() {
		private T instance;

		public T get() {
		    if (instance == null) {
			Log.debug("Creating: " + type.toString());
			this.instance = unscoped.get();
		    }
		    return instance;
		}
	    };
	}
    };

    public static final Scope UNSCOPED = new Scope() {
	public <T> Provider<T> scope(Class<T> type, Provider<T> unscoped) {
	    return unscoped;
	}
    };

    public static Scope SINGLETON_EAGER = new Scope() {
	public <T> Provider<T> scope(Class<T> type, Provider<T> unscoped) {
	    final T instance = unscoped.get();
	    return new Provider<T>() {
		public T get() {
		    return instance;
		}

	    };
	}
    };

    private static final HashMap<Class<?>, Context> contexts = new HashMap<Class<?>, Context>();

    public static Context getConext(final Class<?> contextKey) {
	Context context = contexts.get(contextKey);
	if (context == null) {
	    context = new Context(contextKey);
	    contexts.put(contextKey, context);
	}
	return context;
    }
}
