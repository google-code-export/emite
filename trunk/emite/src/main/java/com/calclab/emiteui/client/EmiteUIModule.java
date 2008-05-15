package com.calclab.emiteui.client;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emiteuiplugin.client.ChatDialogFactory;
import com.calclab.emiteuiplugin.client.ChatDialogFactoryImpl;
import com.calclab.emiteuiplugin.client.EmiteDialog;

public class EmiteUIModule implements Module {

    public static void loadWithDependencies(final ModuleContainer container) {
	container.add(new EmiteUIModule());
    }

    public Class<? extends Module> getType() {
	return EmiteUIModule.class;
    }

    public void onLoad(final Container container) {
	final Provider<Xmpp> xmppPv = container.getProvider(Xmpp.class);

	final ChatDialogFactory factory = container.registerSingletonInstance(ChatDialogFactory.class,
		new ChatDialogFactoryImpl());

	// Atención: Lazy loading y EmiteUI NO es un singleton
	container.registerProvider(EmiteDialog.class, new Provider<EmiteDialog>() {
	    public EmiteDialog get() {
		return new EmiteDialog(DefaultDispatcher.getInstance(), xmppPv.get(), factory);
	    }

	});
    }
}
