package com.calclab.emite.client.modular;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.modular.scopes.Scope;

public class ModuleBuilderTest {

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

    private ModuleBuilder builder;

    @Before
    public void beforeTest() {
	this.builder = new ModuleBuilder();
    }

    @Test
    public void shouldInstallModules() {
	final FakeModule module1 = new FakeModule(Object.class);
	final FakeModule module2 = new FakeModule(Integer.class);
	builder.add(module1, module2);
	assertEquals(1, module1.getTimesLoaded());
	assertEquals(1, module2.getTimesLoaded());
    }

    @Test
    public void shouldNotInstallSameModuleTwice() {
	final FakeModule module = new FakeModule(Object.class);
	builder.add(module, module, module);
	assertEquals(1, module.getTimesLoaded());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldUseScopes() {
	final Scope scope = mock(Scope.class);
	final Provider<Object> provider = mock(Provider.class);
	builder.registerProvider(Object.class, provider, scope);
	verify(scope).scope(same(Object.class), same(provider));
    }
}
