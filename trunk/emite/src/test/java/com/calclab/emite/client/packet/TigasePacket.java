package com.calclab.emite.client.packet;

import java.util.ArrayList;
import java.util.List;

import tigase.xml.Element;

public class TigasePacket extends AbstractPacket {
    private final Element delegate;

    public TigasePacket(final Element element) {
        this.delegate = element;
    }

    public Packet add(final String nodeName, final String xmlns) {
        throw new RuntimeException("not implemented");
    }

    public void addChild(final Packet toBeSend) {
        throw new RuntimeException("not implemented");
    }

    public void addText(final String text) {
        throw new RuntimeException("not implemented");
    }

    public String getAttribute(final String name) {
        return delegate.getAttribute(name);
    }

    public List<? extends Packet> getChildren() {
        final List<Element> children = delegate.getChildren();
        return wrap(children);
    }

    public List<Packet> getChildren(final String name) {
        return wrap(delegate.getChildren(name));
    }

    public Packet getFirstChildren(final String childName) {
        return new TigasePacket(delegate.getChild(childName));
    }

    public String getName() {
        return delegate.getName();
    }

    public Packet getParent() {
        throw new RuntimeException("not implemented");
    }

    public String getText() {
        return delegate.getCData();
    }

    public void render(final StringBuffer buffer) {
        buffer.append(delegate.toString());
    }

    public void setAttribute(final String name, final String value) {
        throw new RuntimeException("not implemented");
    }

    public void setText(final String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    private List<Packet> wrap(final List<Element> children) {
        final ArrayList<Packet> result = new ArrayList<Packet>();
        if (children != null) {
            for (final Element e : children) {
                result.add(new TigasePacket(e));
            }
        }
        return result;
    }
}
