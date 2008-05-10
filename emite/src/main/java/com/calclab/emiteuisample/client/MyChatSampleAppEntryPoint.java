package com.calclab.emiteuisample.client;

import com.calclab.emiteuiplugin.client.EmiteUI;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence;
import com.google.gwt.core.client.EntryPoint;

public class MyChatSampleAppEntryPoint implements EntryPoint {

    public void onModuleLoad() {
        EmiteUI chat = new EmiteUI("admin@localhost", "easyeasy", "/proxy", "rooms.localhost");
        chat.show(OwnPresence.OwnStatus.online);
    }
}