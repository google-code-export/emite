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
package com.calclab.emite.j2se.services;

import java.io.PrintStream;

public class PrintStreamConnectionListener implements HttpConnectorListener {

    private final PrintStream out;

    public PrintStreamConnectionListener() {
	this(System.out);
    }

    public PrintStreamConnectionListener(final PrintStream out) {
	this.out = out;
    }

    public void onError(final String id, final String cause) {
	out.println("CONN # " + id + "-ERROR: " + cause);
    }

    public void onFinish(final String id, final long duration) {
	out.println("CONN FINISHED: " + id + " with duration: " + duration);
    }

    public void onResponse(final String id, final String response) {
	out.println("CONN IN: " + response);
    }

    public void onSend(final String id, final String xml) {
	out.println("CONN OUT: " + xml);
    }

    public void onStart(final String id) {
	out.println("CONN START: " + id);
    }

}
