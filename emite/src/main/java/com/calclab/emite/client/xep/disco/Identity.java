package com.calclab.emite.client.xep.disco;

import com.calclab.emite.client.core.packet.IPacket;

/**
 * Each <identity/> element MUST possess 'category' and 'type' attributes
 * specifying the category and type for the entity, and MAY possess a 'name'
 * attribute specifying a natural-language name for the entity
 * 
 * 
 */
public class Identity {
    public static Identity fromPacket(final IPacket packet) {
	return new Identity(packet.getAttribute("category"), packet.getAttribute("type"), packet.getAttribute("name"));
    }

    public final String category;
    public final String type;
    public final String name;

    public Identity(final String category, final String type, final String name) {
	this.category = category;
	this.type = type;
	this.name = name;
    }
}
