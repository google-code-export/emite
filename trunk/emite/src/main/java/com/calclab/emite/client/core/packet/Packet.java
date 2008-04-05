package com.calclab.emite.client.core.packet;

import java.util.HashMap;
import java.util.List;

/**
 * TODO: no está nada claro lo del setText, getText, addText...
 * 
 * @author dani
 * 
 */
public interface Packet {
    /**
     * 
     * 
     * @param the
     *                attribute name
     * @return the integer value
     * @throws an
     *                 exception
     */
    public int getAttributeAsInt(String name);

    public HashMap<String, String> getAttributes();

    public int getChildrenCount();

    public boolean hasAttribute(String name);

    public boolean hasAttribute(String name, String value);

    public Packet With(final Packet child);

    Packet add(String nodeName, String xmlns);

    void addChild(Packet child);

    void addText(String text);

    String getAttribute(String name);

    List<? extends Packet> getChildren();

    /**
     * Return all the descendant childs with node name
     * 
     * @param name
     */
    List<Packet> getChildren(String name);

    Packet getFirstChild(String childName);

    String getName();

    Packet getParent();

    String getText();

    void render(StringBuffer buffer);

    void setAttribute(String name, String value);

    void setText(String text);

    Packet With(String name, long value);

    /**
     * Chain-able method to add a attribute
     * 
     * @param name
     * @param value
     * @return
     */
    Packet With(String name, String value);

    Packet WithText(String text);

}
