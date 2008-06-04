package com.calclab.seecode;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class FolderJSONWriterTest {

    private StringWriter writer;
    private FolderJSONWriter out;

    @Before
    public void beforeTest() {
	writer = new StringWriter();
	out = new FolderJSONWriter(new PrintWriter(writer));
    }

    @Test
    public void testFolder() {
	final Folder folder = new Folder("com.calcab.emite");
	out.print(folder);
	assertWrited("{'id':'0','name':'emite','data':[],children:[]}");
    }

    @Test
    public void testFolderWithSource() {
	final Folder folder = new Folder("com.calcab.emite");
	folder.addSource(new SourceFile("Source"));
	out.print(folder);
	assertWrited("{'id':'0','name':'emite','data':[],children:[{'id':'1','name':'Source','data':[],children:[]}]}");
    }

    @Test
    public void testSource() {
	final SourceFile file = new SourceFile("FileName");
	out.print(file);
	assertWrited("{'id':'0','name':'FileName','data':[],children:[]}");
    }

    private void assertWrited(final String expected) {
	final String output = writer.toString();
	assertEquals(expected.replaceAll("'", "\""), output);
    }
}
