/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.core.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasicPacket extends AbstractPacket {
	private final HashMap<String, String> attributes;
	private final ArrayList<Packet> children;
	private final String name;
	private BasicPacket parent;

	public BasicPacket(final String name) {
		this(name, null);
	}

	public BasicPacket(final String name, final String xmlns) {
		this.name = name;
		this.attributes = new HashMap<String, String>();
		this.children = new ArrayList<Packet>();
		if (xmlns != null) {
			setAttribute("xmlns", xmlns);
		}
		parent = null;
	}

	public Packet add(final String name, final String xmlns) {
		final BasicPacket child = new BasicPacket(name, xmlns);
		child.parent = this;
		add(child);
		return child;
	}

	public void addChild(final Packet child) {
		children.add(child);
	}

	// TODO: de momento funciona como add
	public void addText(final String value) {
		children.add(new TextPacket(value));
	}

	public String getAttribute(final String name) {
		return attributes.get(name);
	}

	/**
	 * WARNING: broken encapsulation
	 */
	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public List<? extends Packet> getChildren() {
		return children;
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

	public int getChildrenCount() {
		return children.size();
	}

	public Packet getFirstChild(final String childName) {
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

	public Packet getParent() {
		return parent;
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

	public void render(final StringBuffer buffer) {
		buffer.append("<").append(name);

		for (final String key : attributes.keySet()) {
			buffer.append(" ").append(key).append("=\"");
			buffer.append(attributes.get(key)).append("\"");
		}
		if (children.size() > 0) {
			buffer.append(">");
			for (final Packet child : children) {
				child.render(buffer);
			}
			buffer.append("</").append(name).append(">");
		} else {
			buffer.append(" />");
		}
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

	protected void add(final BasicPacket node) {
		children.add(node);
	}
}
