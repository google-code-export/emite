package com.calclab.emite.client.core.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoPacket implements IPacket {
    public static final NoPacket INSTANCE = new NoPacket();
    private static final HashMap<String, String> EMPTY_ATTRIBUTTES = new HashMap<String, String>();
    private static final List<? extends IPacket> EMPTY_CHILDREN = new ArrayList<IPacket>();

    private NoPacket() {

    }

    public IPacket add(final String nodeName, final String xmlns) {
	return this;
    }

    public void addChild(final IPacket child) {
    }

    public void addText(final String text) {
    }

    public String getAttribute(final String name) {
	return null;
    }

    public int getAttributeAsInt(final String name) {
	return 0;
    }

    public HashMap<String, String> getAttributes() {
	return EMPTY_ATTRIBUTTES;
    }

    public List<? extends IPacket> getChildren() {
	return EMPTY_CHILDREN;
    }

    public List<? extends IPacket> getChildren(final String name) {
	return EMPTY_CHILDREN;
    }

    public int getChildrenCount() {
	return 0;
    }

    public IPacket getFirstChild(final String childName) {
	return this;
    }

    public String getName() {
	return null;
    }

    public IPacket getParent() {
	return this;
    }

    public String getText() {
	return null;
    }

    public boolean hasAttribute(final String name) {
	return false;
    }

    public boolean hasAttribute(final String name, final String value) {
	return false;
    }

    public void render(final StringBuffer buffer) {
    }

    public void setAttribute(final String name, final String value) {
    }

    public void setText(final String text) {
    }

    public IPacket With(final IPacket child) {
	return this;
    }

    public IPacket With(final String name, final long value) {
	return this;
    }

    public IPacket With(final String name, final String value) {
	return this;
    }

    public IPacket WithText(final String text) {
	return this;
    }

}
