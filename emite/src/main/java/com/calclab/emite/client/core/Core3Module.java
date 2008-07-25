package com.calclab.emite.client.core;

import com.calclab.emite.client.core.bosh3.Bosh3Connection;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.services.Services;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.SingletonScope;

public class Core3Module extends AbstractModule {

    public Core3Module() {
	super(Core3Module.class);
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<Connection>(Connection.class) {
	    public Connection create() {
		return new Bosh3Connection($(Services.class));
	    }
	});
    }

}
