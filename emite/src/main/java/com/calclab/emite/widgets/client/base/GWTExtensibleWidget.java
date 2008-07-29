package com.calclab.emite.widgets.client.base;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTExtensibleWidget extends DockPanel implements DockableWidget {

    public void dock(final DockPoint point, final EmiteWidget widget) {
	if (point == DockableWidget.EXT_TOP) {
	    add((Widget) widget, DockPanel.NORTH);
	} else if (point == DockableWidget.EXT_RIGHT) {
	    add((Widget) widget, DockPanel.EAST);
	} else {
	    throw new RuntimeException("Dock point " + point + " not found.");
	}
    }

    public void unDock(final EmiteWidget widget) {
	remove((Widget) widget);
    }

}
