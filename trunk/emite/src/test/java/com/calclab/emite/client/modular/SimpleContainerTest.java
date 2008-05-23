package com.calclab.emite.client.modular;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleContainerTest {

    private Container container;

    @Before
    public void beforeTest() {
	container = new HashContainer();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFaillToGetUnregisteredComponent() {
	container.getInstance(Object.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRegisterProviders() {
	final Provider<Object> provider = mock(Provider.class);
	container.registerProvider(Object.class, provider, Scopes.UNSCOPED);
	container.getInstance(Object.class);
	verify(provider).get();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldUseScopes() {
	final Scope scope = mock(Scope.class);
	final Provider<Object> provider = mock(Provider.class);
	container.registerProvider(Object.class, provider, scope);
	verify(scope).scope(same(Object.class), same(provider));
    }
}
