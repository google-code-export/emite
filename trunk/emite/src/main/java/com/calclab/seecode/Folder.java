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

import java.util.ArrayList;
import java.util.List;

public class Folder {
    private static final char DOT = '.';
    private final String qualifiedName;
    private final String name;
    private Folder parent;
    private final List<Folder> children;
    private final ArrayList<SourceFile> sources;

    public Folder(final String qualifiedName) {
	this.qualifiedName = qualifiedName;
	this.name = qualifiedName.substring(qualifiedName.lastIndexOf(DOT) + 1);
	this.children = new ArrayList<Folder>();
	this.sources = new ArrayList<SourceFile>();
    }

    public void addSource(final SourceFile sourceFile) {
	sources.add(sourceFile);
    }

    public List<Folder> getChildren() {
	return children;
    }

    public String getName() {
	return name;
    }

    public Folder getParent() {
	return parent;
    }

    public String getQualifiedName() {
	return qualifiedName;
    }

    public List<SourceFile> getSources() {
	return sources;
    }

    public boolean isRoot() {
	return qualifiedName.indexOf(DOT) < 0;
    }

    public void setParent(final Folder parent) {
	this.parent = parent;
	if (parent != null)
	    parent.addChildren(this);
    }

    @Override
    public String toString() {
	return qualifiedName;
    }

    private void addChildren(final Folder folder) {
	children.add(folder);
    }

}
