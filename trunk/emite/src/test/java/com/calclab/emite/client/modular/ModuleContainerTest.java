package com.calclab.emite.client.modular;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ModuleContainerTest {

    private ModuleContainer container;

    @Before
    public void beforeTest() {
	this.container = new ModuleContainer();
    }

    @Test
    public void shouldInstallModules() {
	final Module module = mock(Module.class);
	container.load(module, module);
	verify(module, times(2)).onLoad(same(container));
    }
}
