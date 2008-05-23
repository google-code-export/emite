package com.calclab.emite.client.core.packet;

import java.util.HashMap;
import java.util.List;

public class PacketRenderer {
    public static String toString(final IPacket packet) {
	final StringBuffer buffer = new StringBuffer();
	toString(packet, buffer);
	return buffer.toString();
    }

    public static void toString(final IPacket root, final StringBuffer buffer) {
	final String name = root.getName();
	buffer.append("<").append(name);
	final HashMap<String, String> attributes = root.getAttributes();
	for (final String key : attributes.keySet()) {
	    final String value = attributes.get(key);
	    if (value != null) {
		buffer.append(" ").append(key).append("=\"");
		buffer.append(value).append("\"");
	    }
	}

	if (root.getText() != null) {
	    buffer.append(">");
	    buffer.append(root.getText());
	    buffer.append("</").append(name).append(">");
	} else {
	    final List<? extends IPacket> children = root.getChildren();
	    if (children.size() > 0) {
		buffer.append(">");
		for (final IPacket child : children) {
		    toString(child, buffer);
		}
		buffer.append("</").append(name).append(">");
	    } else {
		buffer.append(" />");
	    }
	}
    }

}
