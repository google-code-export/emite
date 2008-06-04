package com.calclab.seecode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CodeTest {

    private Code code;

    @Before
    public void beforeTest() {
	code = new Code();
    }

    @Test
    public void shouldCreateSource() {
	final SourceFile source = code.addSource("com/calclab/File.java");
	assertNotNull(source);
	assertEquals("File.java", source.getFileName());
	final Folder folder = source.getParent();
	assertNotNull(folder);
	assertEquals("calclab", folder.getName());
	assertEquals("com.calclab", folder.getQualifiedName());
    }

    @Test
    public void shouldHaveFolders() {
	code.addSource("com/calclab/A.java");
	code.addSource("com/calclab/B.java");
	code.addSource("org/calclab/C.java");
	assertEquals(4, code.getFolders().size());
    }

    @Test
    public void shouldHaveRoots() {
	code.addSource("com/calclab/A.java");
	code.addSource("com/calclab/B.java");
	code.addSource("org/calclab/C.java");
	final List<Folder> roots = code.getRootFolders();
	assertNotNull(roots);
	assertEquals(2, roots.size());
    }

    @Test
    public void shouldHaveSources() {
	code.addSource("com/calclab/A.java");
	code.addSource("com/calclab/B.java");
	final List<SourceFile> sources = code.getSources();
	assertNotNull(sources);
	assertEquals(2, sources.size());
    }

    @Test
    public void shouldWalk() {
	code.addSource("a/A.java");
	code.addSource("b/B.java");
	assertEquals(2, code.getRootFolders().size());
	assertNotNull(code.getRootFolder("a"));
    }

}
