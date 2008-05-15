package com.calclab.emite.client;

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.services.gwt.GWTServicesModule;
import com.calclab.emite.client.extra.avatar.AvatarModule;
import com.calclab.emite.client.extra.muc.MUCModule;
import com.calclab.emite.client.im.InstantMessagingModule;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.xmpp.XMPPModule;

public class EmiteModule implements Module {

    public static Xmpp getXmpp(final Container container) {
	return container.getInstance(Xmpp.class);
    }

    public static void load(final ModuleContainer container) {
	container.add(new GWTServicesModule());
	container.add(new CoreModule(), new XMPPModule(), new InstantMessagingModule());
	// FIXME: esto debería ir fuera de aquí
	container.add(new MUCModule(), new AvatarModule());
	container.add(new EmiteModule());
    }

    public Class<? extends Module> getType() {
	return EmiteModule.class;
    }

    public void onLoad(final Container container) {
	container.registerSingletonInstance(Xmpp.class, new Xmpp(container));
    }

}
