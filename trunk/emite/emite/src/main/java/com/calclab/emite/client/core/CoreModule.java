package com.calclab.emite.client.core;

import com.calclab.emite.client.core.bosh.Bosh3Connection;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.services.Services;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;

// FIXME: refactor CoreModule = BoshModule
public class CoreModule extends AbstractModule {

    public CoreModule() {
	super();
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
