package com.calclab.seecode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class FolderTest {

    @Test
    public void shouldHaveParentAndChildrens() {
	final Folder a = new Folder("a");
	final Folder b = new Folder("a.b");
	b.setParent(a);
	final List<Folder> children = a.getChildren();
	assertNotNull(children);
	assertEquals(1, children.size());
	assertTrue(children.contains(b));
    }

    @Test
    public void shouldKnowIfItsRoot() {
	final Folder folder = new Folder("a.b.c");
	assertFalse(folder.isRoot());
	final Folder root = new Folder("a");
	assertTrue(root.isRoot());
    }

    @Test
    public void shouldReturnName() {
	final Folder folder = new Folder("a.b.c.d");
	assertEquals("a.b.c.d", folder.getQualifiedName());
	assertEquals("d", folder.getName());
    }
}
