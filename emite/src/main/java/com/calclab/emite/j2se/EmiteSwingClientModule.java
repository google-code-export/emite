package com.calclab.emite.j2se;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.j2se.services.J2SEServicesModule;
import com.calclab.emite.j2se.services.PrintStreamConnectionListener;
import com.calclab.emite.j2se.swing.SwingClient;

public class EmiteSwingClientModule implements Module {

    public static void main(final String args[]) {
	final ModuleContainer container = new ModuleContainer();
	container.add(new J2SEServicesModule(new PrintStreamConnectionListener(System.out)));
	EmiteModule.loadWithDependencies(container);
	container.add(new EmiteSwingClientModule());
	container.getInstance(SwingClient.class).start();
    }

    public Class<?> getType() {
	return EmiteSwingClientModule.class;
    }

    public void onLoad(final Container container) {
	final Xmpp xmpp = container.getInstance(Xmpp.class);
	container.registerSingletonInstance(SwingClient.class, new SwingClient(xmpp));
    }
}
