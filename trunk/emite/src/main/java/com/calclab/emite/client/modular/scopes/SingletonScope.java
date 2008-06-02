package com.calclab.emite.client.modular.scopes;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.modular.Provider;

public class SingletonScope implements Scope {

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	return new Provider<T>() {
	    private T instance;

	    public T get() {
		if (instance == null) {
		    Log.debug("Creating: " + type.toString());
		    this.instance = unscoped.get();
		}
		return instance;
	    }
	};
    }
}
