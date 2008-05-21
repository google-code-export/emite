package com.calclab.emite.j2se.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tigase.xml.Element;

import com.calclab.emite.client.core.packet.AbstractPacket;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.TextUtils;

public class TigasePacket extends AbstractPacket {

    private final Element delegate;

    public TigasePacket(final Element element) {
	this.delegate = element;
    }

    public TigasePacket(final String name) {
	this(new Element(name));
    }

    public IPacket addChild(final String nodeName, final String xmlns) {
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
	return wrap(children);
    }

    public int getChildrenCount() {
	final List<Element> children = delegate.getChildren();
	return children != null ? children.size() : 0;
    }

    public String getName() {
	return delegate.getName();
    }

    public String getText() {
	return TextUtils.unescape(delegate.getCData());
    }

    public boolean removeChild(final IPacket child) {
	return delegate.removeChild(((TigasePacket) child).delegate);
    }

    public void render(final StringBuffer buffer) {
	buffer.append(delegate.toString());
    }

    public void setAttribute(final String name, final String value) {
	delegate.setAttribute(name, value);
    }

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
