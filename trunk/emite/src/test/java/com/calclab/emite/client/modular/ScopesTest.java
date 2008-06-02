package com.calclab.emite.client.modular;

import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.client.modular.scopes.Scopes;
import com.calclab.emite.client.modular.scopes.SingletonScope;

public class ScopesTest {

    @Test
    public void singletonScopeShouldReturnSingletons() {
	final Provider<Object> factory = new Provider<Object>() {
	    public Object get() {
		return new Object();
	    }
	};

	final Provider<Object> scoped = Scopes.get(SingletonScope.class).scope(Object.class, factory);
	final Object singleton = scoped.get();
	assertSame(singleton, scoped.get());
	assertSame(singleton, scoped.get());
    }
}
