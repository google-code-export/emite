package com.calclab.emite.client.packet;

class TextPacket extends BasicPacket {
	private final String value;

	public TextPacket(final String value) {
		super(null, null);
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	protected void render(final StringBuffer buffer) {
		buffer.append(value);
	}
}
