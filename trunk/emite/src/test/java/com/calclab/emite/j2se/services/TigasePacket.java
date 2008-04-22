package com.calclab.emite.j2se.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tigase.xml.Element;

import com.calclab.emite.client.core.packet.DSLPacket;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.NoPacket;
import com.calclab.emite.client.core.packet.TextUtils;

public class TigasePacket extends DSLPacket {
    private final Element delegate;

    public TigasePacket(final Element element) {
	this.delegate = element;
    }

    public IPacket add(final String nodeName, final String xmlns) {
	throw new RuntimeException("not implemented");
    }

    // TODO
    public void addChild(final IPacket toBeSend) {
	throw new RuntimeException("not implemented");
    }

    public String getAttribute(final String name) {
	return delegate.getAttribute(name);
    }

    public HashMap<String, String> getAttributes() {
	final HashMap<String, String> atts = new HashMap<String, String>();
	final Map<String, String> src = delegate.getAttributes();
	if (src != null) {
	    atts.putAll(src);
	}
	return atts;
    }

    public List<? extends IPacket> getChildren() {
	final List<Element> children = delegate.getChildren();
	return wrap(children);
    }

    public List<IPacket> getChildren(final String name) {
	return wrap(delegate.getChildren(name));
    }

    public int getChildrenCount() {
	final List<Element> children = delegate.getChildren();
	return children != null ? children.size() : 0;
    }

    public IPacket getFirstChild(final String childName) {
	final Element child = delegate.getChild(childName);
	return child != null ? new TigasePacket(child) : NoPacket.INSTANCE;
    }

    public String getName() {
	return delegate.getName();
    }

    // TODO
    public IPacket getParent() {
	throw new RuntimeException("not implemented");
    }

    public String getText() {
	return TextUtils.unescape(delegate.getCData());
    }

    public void render(final StringBuffer buffer) {
	buffer.append(delegate.toString());
    }

    // TODO
    public void setAttribute(final String name, final String value) {
	throw new RuntimeException("not implemented");
    }

    // TODO
    public void setText(final String text) {
	delegate.setCData(text);
    }

    @Override
    public String toString() {
	return delegate.toString();
    }

    private List<IPacket> wrap(final List<Element> children) {
	final ArrayList<IPacket> result = new ArrayList<IPacket>();
	if (children != null) {
	    for (final Element e : children) {
		result.add(new TigasePacket(e));
	    }
	}
	return result;
    }
}
