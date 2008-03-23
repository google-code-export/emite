package com.calclab.emite.client.log;

public interface Logger {
    int DEBUG = 1;
    int INFO = 2;
    int WARN = 3;
    int ERROR = 4;
    int FATAL = 5;

    void debug(String pattern, Object... params);

    void info(String pattern, Object... params);

    void warn(String pattern, Object... params);

    void error(String pattern, Object... params);

    void fatal(String pattern, Object... params);

    void log(int level, String pattern, Object... params);
}
