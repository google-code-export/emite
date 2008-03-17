package com.calclab.emite.client.log;

public interface Logger {
	int DEBUG = 3;

	void debug(String pattern, Object... params);

	void log(int level, String pattern, Object... params);
}
