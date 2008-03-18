package com.calclab.emite.client.log;

import com.calclab.emite.client.utils.TextHelper;

public class LoggerAdapter implements Logger {
	private final LoggerOutput output;

	public LoggerAdapter(final LoggerOutput output) {
		this.output = output;
	}

	public void debug(final String pattern, final Object... params) {
		log(Logger.DEBUG, pattern, params);
	}

	public void info(final String pattern, final Object... params) {
		log(Logger.INFO, pattern, params);
	}

	public void log(final int level, final String pattern, final Object... params) {
		final String applied = TextHelper.template(pattern, params);
		output.log(level, applied);
	}

}
