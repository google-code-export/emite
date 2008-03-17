package com.calclab.xmpptest.bosh.client.script;

public class Parser {

	public static Script getScript(final String text) {
		String name, content;

		final Script script = new Script();

		final String[] chain = text.split("##");
		for (final String cmd : chain) {
			if (cmd.length() > 0) {
				final int firstLineIndex = cmd.indexOf('\n');
				if (firstLineIndex > 0) {
					name = cmd.substring(0, firstLineIndex);
					content = cmd.substring(firstLineIndex + 1);
				} else {
					name = cmd;
					content = "";
				}
				script.add(new Instruction(name.trim(), content));
			}
		}
		return script;
	}
}
