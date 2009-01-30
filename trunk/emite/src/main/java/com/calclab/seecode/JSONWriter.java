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
package com.calclab.seecode;

import java.io.PrintWriter;

public class JSONWriter {
    private static final String X = "\"";

    private final PrintWriter out;
    private int id;

    public JSONWriter(final PrintWriter out) {
	this.out = out;
	this.id = 0;
    }

    public JSONWriter childsClose() {
	return print("]");
    }

    public JSONWriter childsOpen() {
	return print("children:[");
    }

    public JSONWriter data() {
	return print(X + "data" + X + ":[]");
    }

    public JSONWriter hashClose() {
	return print("}");
    }

    public JSONWriter hashOpen() {
	return print("{");
    }

    public JSONWriter id() {
	return pair("id", id++);
    }

    public JSONWriter pair(final String name, final Object value) {
	return print(X + name + X + ":" + X + value + X);
    };

    public JSONWriter sep() {
	return print(",");
    }

    public JSONWriter write(final String text) {
	return print(text);
    }

    private JSONWriter print(final String text) {
	out.print(text);
	return this;
    }

}
