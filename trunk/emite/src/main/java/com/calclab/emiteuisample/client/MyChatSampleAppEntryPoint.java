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
package com.calclab.emiteuisample.client;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emiteui.client.EmiteUIModule;
import com.calclab.emiteuiplugin.client.EmiteDialog;
import com.calclab.emiteuiplugin.client.status.OwnPresence;
import com.google.gwt.core.client.EntryPoint;

public class MyChatSampleAppEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final ModuleContainer container = new ModuleContainer();
	container.add(new EmiteModule(), new EmiteUIModule());
	final EmiteDialog emiteDialog = container.getInstance(EmiteDialog.class);
	emiteDialog.start("admin@localhost", "easyeasy", "/proxy", "rooms.localhost");
	emiteDialog.show(OwnPresence.OwnStatus.online);
    }
}
