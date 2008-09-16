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

import javax.swing.JFrame;

import com.calclab.emite.core.client.EmiteCoreModule;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.InstantMessagingModule;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.emite.j2se.services.J2SEServicesModule;
import com.calclab.emite.j2se.swing.LoginPanel;
import com.calclab.emite.j2se.swing.SwingClient;
import com.calclab.emite.j2se.swing.login.LoginControl;
import com.calclab.emite.j2se.swing.roster.RosterControl;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.disco.client.DiscoveryModule;
import com.calclab.emite.xep.muc.client.MUCModule;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class EmiteSwingClientModule extends AbstractModule {

    public static void main(final String args[]) {
	Suco.install(new EmiteCoreModule(), new J2SEServicesModule(), new InstantMessagingModule(), new MUCModule(),
		new DiscoveryModule(), new EmiteSwingClientModule());
	Suco.get(SwingClient.class);
    }

    public EmiteSwingClientModule() {
	super();
    }

    @Override
    protected void onLoad() {
	register(Singleton.class, new Factory<SwingClient>(SwingClient.class) {
	    @Override
	    public SwingClient create() {
		return new SwingClient($(JFrame.class), $(LoginPanel.class), $(RosterPanel.class),
			$(ChatManager.class), $(RoomManager.class));
	    }
	});

	register(Singleton.class, new Factory<FrameControl>(FrameControl.class) {
	    @Override
	    public FrameControl create() {
		return new FrameControl($(Session.class));
	    }
	}, new Factory<JFrame>(JFrame.class) {
	    @Override
	    public JFrame create() {
		final JFrame frame = new JFrame("emite swing client");
		$(FrameControl.class).setView(frame);
		return frame;
	    }
	});

	register(Singleton.class, new Factory<LoginControl>(LoginControl.class) {
	    @Override
	    public LoginControl create() {
		return new LoginControl($(Connection.class), $(Session.class), $(PresenceManager.class));
	    }
	}, new Factory<LoginPanel>(LoginPanel.class) {
	    @Override
	    public LoginPanel create() {
		final LoginPanel panel = new LoginPanel($(JFrame.class));
		$(LoginControl.class).setView(panel);
		return panel;
	    }
	}, new Factory<RosterControl>(RosterControl.class) {
	    @Override
	    public RosterControl create() {
		return new RosterControl($(Session.class), $(XRosterManager.class), $(XRoster.class));
	    }
	}, new Factory<RosterPanel>(RosterPanel.class) {
	    @Override
	    public RosterPanel create() {
		final RosterPanel panel = new RosterPanel($(JFrame.class));
		$(RosterControl.class).setView(panel);
		return panel;
	    }
	});
    }
}
