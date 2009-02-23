package com.calclab.emite.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportPackage("emitexmpp")
@ExportClosure
public interface Callback extends Exportable {
    @Export
    public void onEvent(Object param);
}
