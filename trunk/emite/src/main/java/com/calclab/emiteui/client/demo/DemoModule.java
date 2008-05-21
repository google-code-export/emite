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
package com.calclab.emiteui.client.demo;

import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;
import com.calclab.emiteui.client.DemoParameters;

public class DemoModule implements Module {

    public Class<? extends Module> getType() {
	return DemoModule.class;
    }

    public void onLoad(final Container container) {

	container.registerProvider(DemoParameters.class, new Provider<DemoParameters>() {
	    public DemoParameters get() {
		return new DemoParameters();
	    }
	}, Scopes.SINGLETON);

	container.registerProvider(EmiteDemoUI.class, new Provider<EmiteDemoUI>() {
	    public EmiteDemoUI get() {
		return new EmiteDemoUI(container.getInstance(DemoParameters.class));
	    }
	}, Scopes.SINGLETON);

    }

}
