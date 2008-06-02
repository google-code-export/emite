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

import com.calclab.emiteui.client.DemoParameters;
import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.modules.Module;
import com.calclab.modular.client.modules.ModuleBuilder;
import com.calclab.modular.client.scopes.SingletonScope;

public class DemoModule implements Module {

    public Class<? extends Module> getType() {
	return DemoModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {

	builder.registerProvider(DemoParameters.class, new Provider<DemoParameters>() {
	    public DemoParameters get() {
		return new DemoParameters();
	    }
	}, SingletonScope.class);

	builder.registerProvider(EmiteDemoUI.class, new Provider<EmiteDemoUI>() {
	    public EmiteDemoUI get() {
		return new EmiteDemoUI(builder.getInstance(DemoParameters.class));
	    }
	}, SingletonScope.class);

    }

}
