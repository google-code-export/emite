package com.calclab.emite.client.modular.scopes;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.client.modular.Provider;

public class BasicScopesTest {

    public static Provider<Object> getScopedProvider(final Scope scope) {
	return scope.scope(Object.class, new Provider<Object>() {
	    public Object get() {
		return new Object();
	    }
	});
    }

    @Test
    public void testNoScope() {
	final Provider<Object> scoped = BasicScopesTest.getScopedProvider(new NoScope());
	assertNotSame(scoped.get(), scoped.get());
    }

    @Test
    public void testSingleton() {
	final Provider<Object> scoped = BasicScopesTest.getScopedProvider(new SingletonScope());
	assertSame(scoped.get(), scoped.get());
    }
}
