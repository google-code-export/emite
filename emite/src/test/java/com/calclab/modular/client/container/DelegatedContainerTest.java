package com.calclab.modular.client.container;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.modular.client.container.Container;
import com.calclab.modular.client.container.DelegatedContainer;
import com.calclab.modular.client.container.Provider;

public class DelegatedContainerTest {

    private Container delegate;
    private DelegatedContainer container;

    @Before
    public void beforeTest() {
	delegate = mock(Container.class);
	container = new DelegatedContainer(delegate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
	final Provider<Object> provider = mock(Provider.class);
	container.registerProvider(Object.class, provider);
	verify(delegate).registerProvider(same(Object.class), same(provider));
    }
}
