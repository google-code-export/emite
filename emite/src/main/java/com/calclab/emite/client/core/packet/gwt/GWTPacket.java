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
package com.calclab.emite.client.core.packet.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.calclab.emite.client.core.packet.DSLPacket;
import com.calclab.emite.client.core.packet.IPacket;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class GWTPacket extends DSLPacket {
    private static final List<IPacket> EMPTY_LIST = new ArrayList<IPacket>();
    private final Element element;

    public GWTPacket(final Element element) {
	this.element = element;
    }

    public IPacket add(final String nodeName, final String xmlns) {
	final Element child = element.getOwnerDocument().createElement(nodeName);
	element.appendChild(child);
	return new GWTPacket(child);
    }

    public void addChild(final IPacket child) {
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

    public List<? extends IPacket> getChildren() {
	return wrap(element.getChildNodes());
    }

    public List<IPacket> getChildren(final String name) {
	final NodeList nodes = element.getElementsByTagName(name);
	return wrap(nodes);
    }

    public int getChildrenCount() {
	return element.getChildNodes().getLength();
    }

    public IPacket getFirstChild(final String childName) {
	final NodeList nodes = element.getElementsByTagName(childName);
	return nodes.getLength() > 0 ? new GWTPacket((Element) nodes.item(0)) : null;
    }

    public String getName() {
	return element.getNodeName();
    }

    public IPacket getParent() {
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

    private List<IPacket> wrap(final NodeList nodes) {
	int length;
	if (nodes == null || (length = nodes.getLength()) == 0) {
	    return EMPTY_LIST;
	}
	final ArrayList<IPacket> selected = new ArrayList<IPacket>();
	for (int index = 0; index < length; index++) {
	    selected.add(new GWTPacket((Element) nodes.item(index)));
	}
	return selected;
    }
}
