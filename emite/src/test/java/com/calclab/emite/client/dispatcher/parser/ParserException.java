package com.calclab.emite.client.dispatcher.parser;

import java.text.MessageFormat;

public class ParserException extends Exception {
	private static final long serialVersionUID = 1L;

	public ParserException(final String pattern, final Object... args) {
		super(MessageFormat.format(pattern, args));
	}

}
