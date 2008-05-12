package com.calclab.emite.client;

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.extra.avatar.AvatarModule;
import com.calclab.emite.client.extra.muc.MUCModule;
import com.calclab.emite.client.im.InstantMessagingModule;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.xmpp.XMPPModule;

public class EmiteModule implements Module {

    /**
     * Create a container with the default Emite modules installed
     * 
     * @param modules
     *                additional modules to install
     * @return
     */
    public static ModuleContainer create(final Module... modules) {
	final ModuleContainer container = new ModuleContainer();
	container.load(modules);
	container.load(new CoreModule(), new XMPPModule(), new InstantMessagingModule());
	// FIXME: esto debería ir fuera de aquí
	container.load(new MUCModule(), new AvatarModule());
	container.load(new EmiteModule());
	return container;
    }

    public static Xmpp getXmpp(final Container container) {
	return container.getInstance(Xmpp.class);
    }

    public void load(final Container container) {
	container.register(Xmpp.class, new Xmpp(container));
    }

}
