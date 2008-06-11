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
package com.calclab.suco.client.scopes;

import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.HashContainer;

public class Scopes {

    /**
     * use SingletonScope.class directly instead
     * 
     * @deprecated
     * 
     */
    @Deprecated
    public static Class<SingletonScope> SINGLETON = SingletonScope.class;

    private static final HashContainer container;

    static {
	container = new HashContainer();

	container.registerSingletonInstance(SingletonScope.class, new SingletonScope());
	container.registerSingletonInstance(NoScope.class, new NoScope());
    }

    public static <S> S addScope(final Class<S> scopeType, final S scope) {
	return container.registerSingletonInstance(scopeType, scope);
    }

    public static Container get() {
	return container;
    }

    public static <T> T get(final Class<T> scopeType) {
	return container.getInstance(scopeType);
    }

}
