package com.calclab.emite.client.components;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DelegatedContainerTest {

    private Container delegate;
    private DelegatedContainer container;

    @Before
    public void beforeTest() {
	delegate = mock(Container.class);
	container = new DelegatedContainer(delegate);
    }

    @Test
    public void testRegister() {
	final Component component = mock(Component.class);
	container.register("theName", component);
	verify(delegate).register(eq("theName"), same(component));
    }

    @Test
    public void testRegisterInstallable() {
	final Installable installable = mock(Installable.class);
	container.register("theName", installable);
	verify(delegate).register(eq("theName"), same(installable));
    }
}
