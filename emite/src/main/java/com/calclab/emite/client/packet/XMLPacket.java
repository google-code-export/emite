package com.calclab.emite.client.packet;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class XMLPacket extends AbstractPacket {
	private final Element element;

	public XMLPacket(final Element element) {
		this.element = element;
	}

	public Packet add(final String nodeName, final String xmlns) {
		final Element child = element.getOwnerDocument().createElement(nodeName);
		element.appendChild(child);
		return new XMLPacket(child);
	}

	public void addText(final String text) {
		element.appendChild(element.getOwnerDocument().createTextNode(text));
	}

	public String getAttribute(final String name) {
		return element.getAttribute(name);
	}

	public List<? extends Packet> getChildren() {
		return wrap(element.getChildNodes());
	}

	public List<Packet> getChildren(final String name) {
		final NodeList nodes = element.getElementsByTagName(name);
		return wrap(nodes);
	}

	public Packet getFirstChildren(final String childName) {
		final NodeList nodes = element.getElementsByTagName(childName);
		return nodes.getLength() > 0 ? new XMLPacket((Element) nodes.item(0)) : null;
	}

	public String getName() {
		return element.getNodeName();
	}

	public Packet getParent() {
		return new XMLPacket((Element) element.getParentNode());
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
			selected.add(new XMLPacket((Element) nodes.item(index)));
		}
		return selected;
	}

}
