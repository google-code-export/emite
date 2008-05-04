package com.calclab.emite.client.components;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class HashContainerTest {

    private HashContainer container;

    @Before
    public void beforeTest() {
	container = new HashContainer();
    }

    @Test
    public void shouldInstall() {
	final Installable installable = mock(Installable.class);
	container.register("theName", installable);
	assertSame(installable, container.get("theName"));
	container.install();
	verify(installable).install();
    }

    @Test
    public void shouldRegister() {
	final Component component = mock(Component.class);
	container.register("theName", component);
	assertSame(component, container.get("theName"));
    }
}
