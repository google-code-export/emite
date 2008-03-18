package com.calclab.emite.client.log;

public interface Logger {
	int DEBUG = 3;
	int INFO = 2;

	void debug(String pattern, Object... params);

	void info(String pattern, Object... params);

	void log(int level, String pattern, Object... params);
}
