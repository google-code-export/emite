package com.calclab.emite.client.core.packet;

public class Filters {

    public static final PacketFilter ANY = new PacketFilter() {
	public boolean isValid(final IPacket packet) {
	    return true;
	}
    };

    public static PacketFilter byName(final String nodeName) {
	return new PacketFilter() {
	    public boolean isValid(final IPacket packet) {
		return nodeName.equals(packet.getName());
	    }
	};
    }

    public static PacketFilter byNameAndXMLNS(final String nodeName, final String nodeXmls) {
	return new PacketFilter() {
	    public boolean isValid(final IPacket packet) {
		return nodeName.equals(packet.getName()) && packet.hasAttribute("xmlns", nodeXmls);
	    }
	};
    }
}
