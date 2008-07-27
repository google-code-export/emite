package com.calclab.emite.widgets.client.deploy;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.widgets.client.EmiteWidget;
import com.calclab.suco.client.container.Container;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Deployer {

    private final Container container;

    public Deployer(final Container container) {
	this.container = container;
    }

    public void deploy(final String divClass, final Class<? extends EmiteWidget> widgetClass) {
	final ArrayList<Element> elements = findElementsByClass(divClass);
	for (final Element e : elements) {
	    install(e, container.getInstance(widgetClass));
	}
    }

    public ArrayList<Element> findElementsByClass(final String divClass) {
	Log.debug("Finding elements div." + divClass);
	final ArrayList<Element> elements = new ArrayList<Element>();
	final Document document = Document.get();
	final NodeList<Element> divs = document.getElementsByTagName("div");
	for (int index = 0, total = divs.getLength(); index < total; index++) {
	    final Element div = divs.getItem(index);
	    final String[] classes = div.getClassName().split(" ");
	    for (final String className : classes) {
		if (divClass.equals(className)) {
		    elements.add(div);
		}
	    }
	}
	return elements;
    }

    public Settings getData(final Element element, final String prefix, final Settings settings) {
	for (final String key : settings.getKeys()) {
	    settings.set(key, getData(element, prefix, key));
	}
	return settings;
    }

    public Settings getMeta(final String prefix, final Settings settings) {
	for (final String key : settings.getKeys()) {
	    settings.set(key, getMeta(prefix, key));
	}
	return settings;

    }

    public void install(final Element element, final EmiteWidget widget) {
	Log.debug("Installing on element id: " + element.getId());
	setParams(element, widget);
	clearElement(element);
	RootPanel.get(element.getId()).add((Widget) widget);
    }

    private void clearElement(final Element element) {
	final NodeList<Node> childs = element.getChildNodes();
	for (int index = 0, total = childs.getLength(); index < total; index++) {
	    element.removeChild(childs.getItem(index));
	}

    }

    private String getData(final Element element, final String prefix, final String key) {
	return element.getAttribute(prefix + key);
    }

    private String getMeta(final String prefix, final String key) {
	final String name = prefix + key;
	Log.debug("Getting meta: " + name);
	final Element element = DOM.getElementById(name);
	return element.getPropertyString("content");
    }

    private void setParams(final Element element, final EmiteWidget widget) {
	final String[] paramNames = widget.getParamNames();
	if (paramNames != null)
	    for (final String name : paramNames) {
		Log.debug("Param name of widget: " + name);
		final String value = element.getAttribute("data-" + name);
		Log.debug("Value: " + value);
		widget.setParam(name, value);
	    }
    }
}
