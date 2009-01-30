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
package com.calclab.emite.widgets.comenta.client;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class ComentaModule extends AbstractModule implements EntryPoint {
    public void onModuleLoad() {
	Suco.install(this);
	Suco.get(Comenta.class).deploy();
    }

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<Comenta>(Comenta.class) {
	    @Override
	    public Comenta create() {
		return new Comenta($(DomAssist.class), $$(ComentaWidget.class));
	    }
	}, new Factory<ComentaWidget>(ComentaWidget.class) {
	    @Override
	    public ComentaWidget create() {
		return new ComentaWidget();
	    }

	    @Override
	    public void onAfterCreated(final ComentaWidget instance) {
		new ComentaController($(Session.class), $(RoomManager.class), instance);
	    }
	});
    }

}
