package com.calclab.modular.client.scopes.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.scopes.BasicScopesTest;
import com.calclab.modular.client.scopes.context.ContextedScope;

import static org.mockito.Mockito.*;

public class ContextScopeTest {

    private ContextedScope<String> contextedScope;
    private Provider<Object> provider;

    @Before
    public void beforeTest() {
	contextedScope = new ContextedScope<String>(String.class);
	provider = BasicScopesTest.getScopedProvider(contextedScope);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateAllNotPreviouslyCreatedInstances() {
	final ContextedScope<String> scope = new ContextedScope<String>(String.class);
	final Provider<Long> provider1 = mock(Provider.class);
	stub(provider1.get()).toReturn(new Long(1));
	final Provider<Long> scoped1 = scope.scope(Long.class, provider1);
	final Provider<Integer> provider2 = mock(Provider.class);
	scope.scope(Integer.class, provider2);

	scope.setContext("context1");
	scoped1.get();
	scope.createAll();
	verify(provider1, times(1)).get();
	verify(provider2, times(1)).get();
    }

    @Test
    public void shouldGetContext() {
	contextedScope.setContext("context1");
	assertEquals("context1", contextedScope.getContext());
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
    public void shouldSetContextOnNewProviders() {
	contextedScope.setContext("context1");
	final Provider<Object> provider2 = BasicScopesTest.getScopedProvider(contextedScope);
	assertNotNull(provider2.get());
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
