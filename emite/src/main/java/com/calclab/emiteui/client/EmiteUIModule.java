package com.calclab.emiteui.client;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emiteuiplugin.client.EmiteDialog;

public class EmiteUIModule implements Module {

    public static ModuleContainer create() {
	final ModuleContainer container = new ModuleContainer();
	container.load(new EmiteUIModule());
	return container;
    }

    // Atención: Lazy loading y EmiteUI NO es un singleton
    public void onLoad(final Container container) {
	container.registerProvider(EmiteDialog.class, new Provider<EmiteDialog>() {
	    public EmiteDialog get() {
		return new EmiteDialog(DefaultDispatcher.getInstance());
	    }

	});
    }
}
