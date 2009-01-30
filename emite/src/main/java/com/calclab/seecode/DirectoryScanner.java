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

import java.io.File;

public class DirectoryScanner {

    public static interface FileWalker {
	void onFile(File file);
    }

    private final File root;
    private final String pattern;

    public DirectoryScanner(final File root, final String pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    /**
     * Recursive function to descent into the directory tree
     * 
     * @param dir
     *                A file object defining the top directory
     * @param pattern
     */
    public void walk(final File dir, final FileWalker fileWalker) {

	final File children[] = dir.listFiles();
	if (children != null) {
	    for (final File file : children) {
		if (file.isDirectory()) {
		    walk(file, fileWalker);
		} else if (file.getName().endsWith(pattern)) {
		    fileWalker.onFile(file);
		}
	    }

	}
    }

    public void walk(final FileWalker fileWalker) {
	walk(root, fileWalker);
    }

}
