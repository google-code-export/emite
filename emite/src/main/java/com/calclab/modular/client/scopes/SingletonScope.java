package com.calclab.modular.client.scopes;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.modular.client.container.Provider;

/**
 * A singleton scope creates a provider that always returns same instance
 * 
 */
public class SingletonScope implements Scope {

    /**
     * Use Scopes class
     */
    SingletonScope() {
    }

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	return new Provider<T>() {
	    private T instance;

	    public T get() {
		if (instance == null) {
		    Log.debug("Creating singleton instance: " + type.toString());
		    this.instance = unscoped.get();
		}
		return instance;
	    }
	};
    }
}
