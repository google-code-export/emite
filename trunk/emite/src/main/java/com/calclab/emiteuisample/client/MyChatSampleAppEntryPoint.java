package com.calclab.emiteuisample.client;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emiteui.client.EmiteUIModule;
import com.calclab.emiteuiplugin.client.EmiteDialog;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence;
import com.google.gwt.core.client.EntryPoint;

public class MyChatSampleAppEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final ModuleContainer container = new ModuleContainer();
	container.add(new EmiteModule(), new EmiteUIModule());
	final EmiteDialog emiteDialog = container.getInstance(EmiteDialog.class);
	emiteDialog.start("admin@localhost", "easyeasy", "/proxy", "rooms.localhost");
	emiteDialog.show(OwnPresence.OwnStatus.online);
    }
}
