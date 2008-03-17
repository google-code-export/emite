package com.calclab.emite.client.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasicPacket implements Packet {
	private final HashMap<String, String> attributes;
	private final ArrayList<BasicPacket> children;
	private final String name;

	public BasicPacket(final String name, final String xmlns) {
		this.name = name;
		this.attributes = new HashMap<String, String>();
		this.children = new ArrayList<BasicPacket>();
		if (xmlns != null) {
			setAttribute("xmlns", xmlns);
		}
	}

	public Packet add(final String name, final String xmlns) {
		final BasicPacket child = new BasicPacket(name, xmlns);
		add(child);
		return child;
	}

	// TODO: de momento funciona como add
	public void addText(final String value) {
		children.add(new TextPacket(value));
	}

	public String getAttribute(final String name) {
		return attributes.get(name);
	}

	public List<Packet> getChildren(final String name) {
		final List<Packet> selected = new ArrayList<Packet>();
		for (final Packet child : children) {
			if (name.equals(child.getName())) {
				selected.add(child);
			}
		}
		return selected;
	}

	public Packet getFirstChildren(final String childName) {
		for (final Packet child : children) {
			if (childName.equals(child.getName())) {
				return child;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	/**
	 * si tiene un hijo de texto, lo devuelve
	 */
	public String getText() {
		for (final Packet child : children) {
			if (child.getName() == null) {
				return child.toString();
			}
		}
		return null;
	}

	public void setAttribute(final String name, final String value) {
		attributes.put(name, value);
	}

	// FIXME
	public void setText(final String text) {
		addText(text);
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		render(buffer);
		return buffer.toString();
	}

	public Packet with(final String name, final String value) {
		setAttribute(name, value);
		return this;
	}

	protected void add(final BasicPacket node) {
		children.add(node);
	}

	protected void render(final StringBuffer buffer) {
		buffer.append("<").append(name);

		for (final String key : attributes.keySet()) {
			buffer.append(" ").append(key).append("=\"");
			buffer.append(attributes.get(key)).append("\"");
		}
		if (children.size() > 0) {
			buffer.append(">");
			for (final BasicPacket child : children) {
				child.render(buffer);
			}
			buffer.append("</").append(name).append(">");
		} else {
			buffer.append(" />");
		}
	}
}
