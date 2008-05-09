package com.calclab.emite.client;

import com.calclab.emite.client.container.Container;
import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.extra.avatar.AvatarModule;
import com.calclab.emite.client.extra.muc.MUCModule;
import com.calclab.emite.client.im.InstantMessagingModule;
import com.calclab.emite.client.xmpp.XMPPModule;

public class EmiteModule {

    public static Xmpp load(final Container container) {
	CoreModule.load(container);
	XMPPModule.load(container);
	InstantMessagingModule.load(container);
	// TODO: not here!
	MUCModule.install(container);
	AvatarModule.load(container);

	final Xmpp xmpp = new Xmpp(container);
	container.register(Xmpp.class, xmpp);
	return xmpp;
    }

}
