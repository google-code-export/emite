package com.calclab.xmpptest.bosh.client.script;

public class Instruction {
	private final String content;
	private final String name;

	public Instruction(final String name, final String content) {
		this.name = name;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

}
