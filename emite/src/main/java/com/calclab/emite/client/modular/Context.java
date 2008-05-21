package com.calclab.emite.client.modular;

public class Context implements Scope {

    public Context(final Class<?> contextKey) {
    }

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	return null;
    }

}
