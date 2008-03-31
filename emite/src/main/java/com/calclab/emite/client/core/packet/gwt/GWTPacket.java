package com.calclab.emite.client.core.packet.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.calclab.emite.client.core.packet.AbstractPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class GWTPacket extends AbstractPacket {
	private final Element element;

	public GWTPacket(final Element element) {
		this.element = element;
	}

	public Packet add(final String nodeName, final String xmlns) {
		final Element child = element.getOwnerDocument().createElement(nodeName);
		element.appendChild(child);
		return new GWTPacket(child);
	}

	public void addChild(final Packet child) {
		final GWTPacket p = (GWTPacket) child;
		element.appendChild(p.element);
	}

	public void addText(final String text) {
		element.appendChild(element.getOwnerDocument().createTextNode(text));
	}

	public String getAttribute(final String name) {
		return element.getAttribute(name);
	}

	public HashMap<String, String> getAttributes() {
		throw new RuntimeException("GWTPacket.getAttributes: not implemented!");
	}

	public Map<String, String> getAttributtes() {
		final HashMap<String, String> attributes = new HashMap<String, String>();
		final NamedNodeMap original = element.getAttributes();
		for (int index = 0; index < original.getLength(); index++) {
			final Node node = original.item(index);
			attributes.put(node.getNodeName(), node.getNodeValue());
		}
		return attributes;
	}

	public List<? extends Packet> getChildren() {
		return wrap(element.getChildNodes());
	}

	public List<Packet> getChildren(final String name) {
		final NodeList nodes = element.getElementsByTagName(name);
		return wrap(nodes);
	}

	public int getChildrenCount() {
		return element.getChildNodes().getLength();
	}

	public Packet getFirstChild(final String childName) {
		final NodeList nodes = element.getElementsByTagName(childName);
		return nodes.getLength() > 0 ? new GWTPacket((Element) nodes.item(0)) : null;
	}

	public String getName() {
		return element.getNodeName();
	}

	public Packet getParent() {
		return new GWTPacket((Element) element.getParentNode());
	}

	public String getText() {
		Node item;
		final NodeList childs = element.getChildNodes();
		for (int index = 0; index < childs.getLength(); index++) {
			item = childs.item(index);
			if (item.getNodeType() == Node.TEXT_NODE) {
				return item.getNodeValue();
			}
		}
		return null;
	}

	// FIXME
	public void render(final StringBuffer buffer) {
		buffer.append(element.toString());
	}

	public void setAttribute(final String name, final String value) {
		element.setAttribute(name, value);
	}

	// FIXME
	public void setText(final String text) {
		addText(text);
	}

	private List<Packet> wrap(final NodeList nodes) {
		final ArrayList<Packet> selected = new ArrayList<Packet>();
		for (int index = 0; index < nodes.getLength(); index++) {
			selected.add(new GWTPacket((Element) nodes.item(index)));
		}
		return selected;
	}
}
