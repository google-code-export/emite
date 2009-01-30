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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SeeCode {

    public static void main(final String args[]) throws IOException {
	final File data = getDirectory("./seecode/data");
	final File src = getDirectory("./src/main/java");
	final String pattern = ".java";
	final Code code = new Code();
	new DirectoryScanner(src, pattern).walk(new CodeFileWalker(code, src));
	final Folder root = code.getRootFolder("com");
	final String dataFileName = data.getAbsolutePath() + "/seecode-data.js";
	System.out.println(dataFileName);
	final FileWriter writer = new FileWriter(dataFileName);
	new FolderJSONWriter(new PrintWriter(writer)).write(root);
	writer.close();
    }

    private static File getDirectory(final String path) {
	final File file = new File(path);
	if (file.exists() && file.isDirectory()) {
	    return file;
	} else {
	    throw new RuntimeException(path + " folder not found: " + file);
	}
    }

}
