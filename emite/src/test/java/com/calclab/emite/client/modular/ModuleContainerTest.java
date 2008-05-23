package com.calclab.emite.client.modular;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ModuleContainerTest {

    public class FakeModule implements Module {
	private int loaded;
	private final Class<?> type;

	public FakeModule(final Class<?> type) {
	    this.type = type;
	    loaded = 0;
	}

	public int getTimesLoaded() {
	    return loaded;
	}

	public Class<?> getType() {
	    return type;
	}

	public void onLoad(final ModuleBuilder builder) {
	    loaded++;
	}

    }

    private ModuleBuilder container;

    @Before
    public void beforeTest() {
	this.container = new ModuleBuilder();
    }

    @Test
    public void shouldInstallModules() {
	final FakeModule module1 = new FakeModule(Object.class);
	final FakeModule module2 = new FakeModule(Integer.class);
	container.add(module1, module2);
	assertEquals(1, module1.getTimesLoaded());
	assertEquals(1, module2.getTimesLoaded());
    }

    @Test
    public void shouldNotInstallSameModuleTwice() {
	final FakeModule module = new FakeModule(Object.class);
	container.add(module, module, module);
	assertEquals(1, module.getTimesLoaded());
    }
}
