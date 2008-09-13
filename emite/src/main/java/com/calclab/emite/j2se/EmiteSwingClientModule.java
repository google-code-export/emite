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
package com.calclab.emite.j2se;

import com.calclab.emite.core.client.EmiteCoreModule;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.InstantMessagingModule;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterManager;
import com.calclab.emite.j2se.services.J2SEServicesModule;
import com.calclab.emite.j2se.swing.SwingClient;
import com.calclab.emite.xep.disco.client.DiscoveryModule;
import com.calclab.emite.xep.muc.client.MUCModule;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.module.ModuleManager.ProviderRegisterStrategy;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;

public class EmiteSwingClientModule extends AbstractModule {

    public static void main(final String args[]) {
	Suco.install(ProviderRegisterStrategy.registerOrOverride, new EmiteCoreModule(), new J2SEServicesModule(),
		new InstantMessagingModule(), new MUCModule(), new DiscoveryModule(), new EmiteSwingClientModule());
	Suco.get(SwingClient.class).start();
    }

    public EmiteSwingClientModule() {
	super();
    }

    @Override
    protected void onLoad() {
	register(SingletonScope.class, new Factory<SwingClient>(SwingClient.class) {
	    public SwingClient create() {
		return new SwingClient($(Connection.class), $(Session.class), $(PresenceManager.class),
			$(RosterManager.class), $(Roster.class), $(ChatManager.class), $(RoomManager.class));
	    }
	});
    }
}
