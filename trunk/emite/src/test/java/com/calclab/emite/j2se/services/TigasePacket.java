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
    private static interface Filter {

	boolean isValid(Element e);

    }

    private final Element delegate;

    public TigasePacket(final Element element) {
	this.delegate = element;
    }

    public TigasePacket(final String name) {
	this(new Element(name));
    }

    public IPacket add(final String nodeName, final String xmlns) {
	final TigasePacket child = new TigasePacket(nodeName);
	child.setAttribute("xmlns", xmlns);
	addChild(child);
	return child;
    }

    public void addChild(final IPacket child) {
	final TigasePacket tigaseChild = (TigasePacket) child;
	delegate.addChild(tigaseChild.delegate);
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
	return wrap(children, new Filter() {
	    public boolean isValid(final Element e) {
		return true;
	    }

	});
    }

    public List<IPacket> getChildren(final String name) {
	final List<Element> children = delegate.getChildren();
	return wrap(children, new Filter() {
	    public boolean isValid(final Element e) {
		return e.getName().equals(name);
	    }

	});
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
	delegate.setAttribute(name, value);
    }

    // TODO
    public void setText(final String text) {
	delegate.setCData(text);
    }

    @Override
    public String toString() {
	return delegate.toString();
    }

    private List<IPacket> wrap(final List<Element> children, final Filter filter) {
	final ArrayList<IPacket> result = new ArrayList<IPacket>();
	if (children != null) {
	    for (final Element e : children) {
		if (filter.isValid(e))
		    result.add(new TigasePacket(e));
	    }
	}
	return result;
    }
}
