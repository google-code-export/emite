package com.calclab.emite.client.container;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.container.HashContainer;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class HashContainerTest {

    private HashContainer container;

    @Before
    public void beforeTest() {
	container = new HashContainer();
    }

    @Test
    public void shouldRegister() {
	final Object component = mock(Object.class);
	container.register(Object.class, component);
	assertSame(component, container.get(Object.class));
    }
}
