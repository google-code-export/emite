package com.calclab.emite.client.modular.scopes;

import com.calclab.emite.client.modular.Provider;

public class NoScope implements Scope {

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	return unscoped;
    }

}
