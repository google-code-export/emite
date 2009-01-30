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

public class FolderJSONWriter {

    private final JSONWriter out;

    public FolderJSONWriter(final PrintWriter writer) {
	this.out = new JSONWriter(writer);
    }

    public void write(final Folder root) {
	out.write("var json=");
	print(root);
	out.write(";");
    }

    void print(final Folder root) {
	out.hashOpen().id().sep();
	out.pair("name", root.getName()).sep().data().sep();
	out.childsOpen();
	boolean shouldSeparate = false;
	for (final SourceFile source : root.getSources()) {
	    shouldSeparate = checkFirst(shouldSeparate);
	    print(source);
	}
	shouldSeparate = root.getSources().size() > 0;
	for (final Folder child : root.getChildren()) {
	    shouldSeparate = checkFirst(shouldSeparate);
	    print(child);
	}
	out.childsClose();
	out.hashClose();
    }

    void print(final SourceFile source) {
	out.hashOpen().id().sep();
	out.pair("name", source.getFileName()).sep().data().sep();
	out.childsOpen().childsClose().hashClose();
    }

    private boolean checkFirst(final boolean shouldSeparate) {
	if (shouldSeparate) {
	    out.sep();
	}
	return true;
    }

}
