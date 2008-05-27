package com.calclab.emite.client.xep.disco;

import com.calclab.emite.client.core.packet.IPacket;

public class Feature {

    public static Feature fromPacket(final IPacket packet) {
	return new Feature(packet.getAttribute("val"));
    }

    public final String var;

    public Feature(final String var) {
	this.var = var;
    }

}
