package com.calclab.emite.client.packet;

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

	Packet getFirst(String childName);

	String getName();

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
