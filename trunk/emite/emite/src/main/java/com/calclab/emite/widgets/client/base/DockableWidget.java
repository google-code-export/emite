package com.calclab.emite.widgets.client.base;

public interface DockableWidget {
    public static final DockPoint EXT_TOP = new DockPoint("emiteDock.top");
    public static final DockPoint EXT_RIGHT = new DockPoint("emiteDock.right");

    public void dock(DockPoint point, EmiteWidget widget);

    public void unDock(EmiteWidget widget);
}
