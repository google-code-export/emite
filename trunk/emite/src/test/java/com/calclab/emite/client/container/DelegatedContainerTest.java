package com.calclab.emite.client.container;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.container.Container;
import com.calclab.emite.client.container.DelegatedContainer;

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
	final Object component = mock(Object.class);
	container.register(Object.class, component);
	verify(delegate).register(same(Object.class), same(component));
    }

}
