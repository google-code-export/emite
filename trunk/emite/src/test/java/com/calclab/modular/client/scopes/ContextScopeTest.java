package com.calclab.modular.client.scopes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.scopes.ContextedScope;

import static org.mockito.Mockito.*;

public class ContextScopeTest {

    private ContextedScope<String> contextedScope;
    private Provider<Object> provider;

    @Before
    public void beforeTest() {
	contextedScope = new ContextedScope<String>();
	provider = BasicScopesTest.getScopedProvider(contextedScope);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateAllInstances() {
	final ContextedScope<String> scope = new ContextedScope<String>();
	final Provider<Long> p1 = mock(Provider.class);
	scope.scope(Long.class, p1);
	final Provider<Integer> p2 = mock(Provider.class);
	scope.scope(Integer.class, p2);

	scope.setContext("context1");
	scope.createAll();
	verify(p1).get();
	verify(p2).get();
    }

    @Test
    public void shouldHaveOneInstancePerContext() {
	contextedScope.setContext("context1");
	final Object instance1 = provider.get();
	contextedScope.setContext("context2");
	final Object instance2 = provider.get();
	assertNotSame(instance1, instance2);
	contextedScope.setContext("context1");
	assertSame(instance1, provider.get());
	contextedScope.setContext("context2");
	assertSame(instance2, provider.get());
    }

    @Test
    public void shouldSingletonInSameContext() {
	contextedScope.setContext("context1");
	assertNotNull(provider.get());
	assertSame(provider.get(), provider.get());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenNoContext() {
	provider.get();
    }

}
