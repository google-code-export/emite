package com.calclab.emite.client.modular;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class HashContainerTest {

    private SimpleContainer container;

    @Before
    public void beforeTest() {
	container = new SimpleContainer();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFaillToGetUnregisteredComponent() {
	container.get(Object.class);
    }

    @Test
    public void shouldRegister() {
	final Object component = mock(Object.class);
	container.register(Object.class, component);
	assertSame(component, container.get(Object.class));
    }

}
