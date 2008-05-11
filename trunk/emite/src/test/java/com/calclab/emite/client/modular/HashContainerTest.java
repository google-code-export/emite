package com.calclab.emite.client.modular;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class HashContainerTest {

    private BasicContainer container;

    @Before
    public void beforeTest() {
	container = new BasicContainer();
    }

    @Test(expected = RuntimeException.class)
    public void shouldFaillToGetUnregisteredComponent() {
	container.get(Object.class);
    }

    @Test
    public void shouldInstallModules() {
	final Module module = mock(Module.class);
	container.install(module);
	verify(module).load(same(container));
    }

    @Test
    public void shouldRegister() {
	final Object component = mock(Object.class);
	container.register(Object.class, component);
	assertSame(component, container.get(Object.class));
    }

}
