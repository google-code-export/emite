package com.calclab.emite.widgets.comenta.client;

import java.util.ArrayList;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.suco.client.ioc.Provider;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class Comenta {
    private final DomAssist domAssist;
    private final Provider<ComentaWidget> widgetProvider;

    public Comenta(final DomAssist domAssist, final Provider<ComentaWidget> widgetProvider) {
	this.domAssist = domAssist;
	this.widgetProvider = widgetProvider;
    }

    public void deploy() {
	final ArrayList<Element> elements = domAssist.findElementsByClass("emite-widgets-comenta");
	for (final Element element : elements) {
	    domAssist.clearElement(element);
	    final ComentaWidget widget = widgetProvider.get();
	    domAssist.setProperties(element, widget);
	    RootPanel.get(element.getId()).add(widget);
	}
    }

}
