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
