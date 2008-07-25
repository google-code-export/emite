package com.calclab.emite.client.core;

import com.calclab.emite.client.core.bosh3.Bosh3Connection;
import com.calclab.emite.client.services.Services;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.SingletonFactory;

public class Core3Module extends AbstractModule {

    public Core3Module() {
	super(CoreModule.class);
    }

    @Override
    public void onLoad() {
	register(new SingletonFactory<Bosh3Connection>(Bosh3Connection.class) {
	    public Bosh3Connection create() {
		return new Bosh3Connection($(Services.class));
	    }
	});
    }

}
