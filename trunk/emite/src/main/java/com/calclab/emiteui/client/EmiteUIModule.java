package com.calclab.emiteui.client;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emiteuiplugin.client.EmiteUIFactory;
import com.calclab.emiteuiplugin.client.EmiteDialog;

public class EmiteUIModule implements Module {

    public static void loadWithDependencies(final ModuleContainer container) {
	container.add(new EmiteUIModule());
    }

    public Class<? extends Module> getType() {
	return EmiteUIModule.class;
    }

    public void onLoad(final Container container) {
	final Xmpp xmpp = container.getInstance(Xmpp.class);

	final I18nTranslationService i18n = container.registerSingletonInstance(I18nTranslationService.class,
		new I18nTranslationServiceMocked());

	final EmiteUIFactory factory = container.registerSingletonInstance(EmiteUIFactory.class,
		new EmiteUIFactory(xmpp, i18n));

	container.registerProvider(EmiteDialog.class, new Provider<EmiteDialog>() {
	    public EmiteDialog get() {
		return new EmiteDialog(xmpp, factory);
	    }

	});
    }
}
