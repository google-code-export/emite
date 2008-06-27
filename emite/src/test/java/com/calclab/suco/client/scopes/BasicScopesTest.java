package com.calclab.suco.client.scopes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.suco.client.container.Provider;

public class BasicScopesTest {

    public static Provider<Object> getScopedProvider(final Scope scope) {
	return scope.scope(Object.class, new Provider<Object>() {
	    public Object get() {
		return new Object();
	    }
	});
    }

    @Test
    public void scopesShouldHaveBasicScopes() {
	assertNotNull(Scopes.get(NoScope.class));
	assertNotNull(Scopes.get(SingletonScope.class));
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
