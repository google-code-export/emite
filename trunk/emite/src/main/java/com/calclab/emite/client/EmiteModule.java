package com.calclab.emite.client;

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.extra.avatar.AvatarModule;
import com.calclab.emite.client.extra.muc.MUCModule;
import com.calclab.emite.client.im.InstantMessagingModule;
import com.calclab.emite.client.modular.BasicContainer;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.xmpp.XMPPModule;

public class EmiteModule implements Module {

    public static BasicContainer create(final Module... modules) {
	final BasicContainer container = new BasicContainer();
	container.install(modules);
	container.install(new CoreModule(), new XMPPModule(), new InstantMessagingModule());
	container.install(new MUCModule(), new AvatarModule());
	container.install(new EmiteModule());
	return container;
    }

    public static Xmpp getXmpp(final Container container) {
	return container.get(Xmpp.class);
    }

    public void load(final Container container) {
	container.register(Xmpp.class, new Xmpp(container));
    }

}
