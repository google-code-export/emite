package com.calclab.emite.client.core;

import com.calclab.emite.client.core.bosh3.Bosh3Connection;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.services.Services;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.SingletonFactory;

public class Core3Module extends AbstractModule {

    public Core3Module() {
	super(Core3Module.class);
    }

    @Override
    public void onLoad() {
	register(new SingletonFactory<Connection>(Connection.class) {
	    public Connection create() {
		return new Bosh3Connection($(Services.class));
	    }
	});
    }

}
