package com.calclab.emite.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;

@Export
@ExportPackage("emitexmpp")
@ExportClosure
public interface Callback {
    public void onEvent(String param);
}
