package com.calclab.suco.client.scopes;

import com.calclab.suco.client.container.Provider;

/**
 * The NoScope scope creates a provider that creates a new instance each request
 * 
 */
public class NoScope implements Scope {

    /**
     * Use Scopes class
     */
    NoScope() {
    }

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	return unscoped;
    }

}
