package com.calclab.emite.client.modular;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.DelegatedContainer;

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
	container.registerSingletonInstance(Object.class, component);
	verify(delegate).registerSingletonInstance(same(Object.class), same(component));
    }

}
