package com.calclab.emite.widgets.habla.client;

import java.util.ArrayList;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.browser.client.HasProperties;
import com.calclab.emite.widgets.comenta.client.ComentaWidget;
import com.calclab.suco.client.ioc.Provider;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Habla {

    private final DomAssist domAssist;
    private final Provider<HablaWidget> widgetProvider;

    public Habla(final DomAssist domAssist, final Provider<HablaWidget> widgetProvider) {
	this.domAssist = domAssist;
	this.widgetProvider = widgetProvider;
    }

    public void deploy() {
	final ArrayList<Element> elements = domAssist.findElementsByClass("emite-widgets-habla");
	for (final Element element : elements) {
	    // domAssist.clearElement(element);
	    final HablaWidget widget = widgetProvider.get();
	    domAssist.setProperties(element, widget);
	    RootPanel.get(element.getId()).add(widget);
	}
    }

}
