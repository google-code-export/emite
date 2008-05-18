package com.calclab.emite.client.modular;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

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