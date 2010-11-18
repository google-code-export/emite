/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.suco.client.Suco;
import com.google.gwt.core.client.EntryPoint;

public class EmiteWidgetsEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	Log.debug("Emite widgets loading...");
	Suco.install(new EmiteWidgetsModule());

	Log.debug("Emite widgets deploying...");
	Suco.get(AutoDeploy.class);
	Log.debug("Emite widgets ready.");

    }

}
