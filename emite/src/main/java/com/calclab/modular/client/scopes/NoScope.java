package com.calclab.modular.client.scopes;

import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.scopes.Scope;

/**
 * The NoScope scope creates a provider that creates a new instance each request
 * 
 */
public class NoScope implements Scope {

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	return unscoped;
    }

}
