package com.calclab.emite.j2se;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleBuilder;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;
import com.calclab.emite.client.xep.muc.MUCModule;
import com.calclab.emite.j2se.services.J2SEServicesModule;
import com.calclab.emite.j2se.swing.SwingClient;

public class EmiteSwingClientModule implements Module {

    public static void main(final String args[]) {
	final ModuleBuilder container = new ModuleBuilder();
	container.add(new J2SEServicesModule(), new EmiteModule(), new MUCModule());
	container.add(new EmiteSwingClientModule());
	container.getInstance(SwingClient.class).start();
    }

    public Class<?> getType() {
	return EmiteSwingClientModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	final Xmpp xmpp = builder.getInstance(Xmpp.class);

	builder.registerProvider(SwingClient.class, new Provider<SwingClient>() {
	    public SwingClient get() {
		return new SwingClient(xmpp);
	    }
	}, Scopes.SINGLETON);
    }
}
