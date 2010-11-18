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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Code {

    private final HashMap<String, Folder> foldersByQualifiedName;
    private final ArrayList<SourceFile> sources;
    private final ArrayList<Folder> roots;

    public Code() {
	this.foldersByQualifiedName = new HashMap<String, Folder>();
	this.sources = new ArrayList<SourceFile>();
	this.roots = new ArrayList<Folder>();
    }

    public SourceFile addSource(final String fileName) {
	final String[] splitted = fileName.split("/");
	final SourceFile sourceFile = addSourceFile(splitted);
	final Folder parent = getParentFolder(splitted);
	sourceFile.setParent(parent);
	parent.addSource(sourceFile);
	return sourceFile;
    }

    public Collection<Folder> getFolders() {
	return foldersByQualifiedName.values();
    }

    public Folder getRootFolder(final String name) {
	Folder folder = null;
	for (final Folder root : roots) {
	    if (name.equals(root.getName())) {
		folder = root;
	    }
	}
	return folder;
    }

    public List<Folder> getRootFolders() {
	return roots;
    }

    public List<SourceFile> getSources() {
	return sources;
    }

    private SourceFile addSourceFile(final String[] splitted) {
	final SourceFile sourceFile = new SourceFile(splitted[splitted.length - 1]);
	sources.add(sourceFile);
	return sourceFile;
    }

    private Folder getFolder(final String qualifiedName, final Folder parent) {
	Folder folder = foldersByQualifiedName.get(qualifiedName);
	if (folder == null) {
	    folder = new Folder(qualifiedName);
	    foldersByQualifiedName.put(qualifiedName, folder);
	    folder.setParent(parent);
	    if (folder.isRoot())
		roots.add(folder);
	}
	return folder;
    }

    private Folder getParentFolder(final String[] splitted) {
	final List<String> folders = new ArrayList<String>(Arrays.asList(splitted));
	folders.remove(splitted.length - 1);
	Folder parent = null;
	for (final String folderName : folders) {
	    final String prefix = parent != null ? parent.getQualifiedName() + "." : "";
	    final Folder child = getFolder(prefix + folderName, parent);
	    parent = child;
	}
	return parent;
    }

}
