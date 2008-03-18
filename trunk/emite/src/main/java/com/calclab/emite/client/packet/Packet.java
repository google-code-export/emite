package com.calclab.emite.client.packet;

import java.util.List;

/**
 * TODO: no está nada claro lo del setText, getText, addText...
 * 
 * @author dani
 * 
 */
public interface Packet {
	Packet add(String nodeName, String xmlns);

	void addText(String text);

	String getAttribute(String name);

	List<? extends Packet> getChildren();

	/**
	 * Return all the descendant childs with node name
	 * 
	 * @param name
	 */
	List<Packet> getChildren(String name);

	Packet getFirstChildren(String childName);

	String getName();

	Packet getParent();

	String getText();

	void setAttribute(String name, String value);

	void setText(String text);

	/**
	 * helper method
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	Packet with(String name, String value);
}
