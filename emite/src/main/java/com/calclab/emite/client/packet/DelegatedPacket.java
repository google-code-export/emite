package com.calclab.emite.client.packet;

import java.util.List;

public class DelegatedPacket extends AbstractPacket {
	private final Packet delegate;

	public DelegatedPacket(final Packet delegate) {
		this.delegate = delegate;
	}

	public final Packet add(final String nodeName, final String xmlns) {
		return delegate.add(nodeName, xmlns);
	}

	public void addChild(final Packet child) {
		delegate.addChild(child);
	}

	public final void addText(final String text) {
		delegate.addText(text);
	}

	public final String getAttribute(final String name) {
		return delegate.getAttribute(name);
	}

	public List<? extends Packet> getChildren() {
		return delegate.getChildren();
	}

	public List<Packet> getChildren(final String name) {
		return delegate.getChildren(name);
	}

	public int getChildrenCount() {
		return delegate.getChildrenCount();
	}

	public final Packet getFirstChild(final String childName) {
		return delegate.getFirstChild(childName);
	}

	public final String getName() {
		return delegate.getName();
	}

	public Packet getParent() {
		return delegate.getParent();
	}

	public final String getText() {
		return delegate.getText();
	}

	public void render(final StringBuffer buffer) {
		delegate.render(buffer);
	}

	public final void setAttribute(final String name, final String value) {
		delegate.setAttribute(name, value);
	}

	public void setText(final String text) {
		delegate.setText(text);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
