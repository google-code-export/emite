package com.calclab.modular.client.container;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.modular.client.container.Container;
import com.calclab.modular.client.container.HashContainer;
import com.calclab.modular.client.container.Provider;

public class HashContainerTest {

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
	container.registerProvider(Object.class, provider);
	container.getInstance(Object.class);
	verify(provider).get();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTestIfExsists() {
	assertFalse(container.hasProvider(Object.class));
	final Provider<Object> provider = mock(Provider.class);
	container.registerProvider(Object.class, provider);
	assertTrue(container.hasProvider(Object.class));
    }

}
