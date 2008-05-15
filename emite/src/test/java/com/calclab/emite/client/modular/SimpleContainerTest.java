package com.calclab.emite.client.modular;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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

    @Test
    public void shouldRegister() {
	final Object component = mock(Object.class);
	container.registerSingletonInstance(Object.class, component);
	assertSame(component, container.getInstance(Object.class));
	assertNotNull(container.getProvider(Object.class));
    }

    @Test
    public void shouldRegisterProviders() {
	final Provider<Object> provider = mock(Provider.class);
	container.registerProvider(Object.class, provider);
	container.getInstance(Object.class);
	verify(provider).get();
    }

}
