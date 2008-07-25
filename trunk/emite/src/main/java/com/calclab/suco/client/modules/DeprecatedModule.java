package com.calclab.suco.client.modules;

import com.calclab.suco.client.container.Container;

public abstract class DeprecatedModule implements Module {

    public void onLoad(final Container container) {
	onLoad(new ModuleBuilder(container));
    }

    public abstract void onLoad(final ModuleBuilder builder);

}
