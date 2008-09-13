package com.calclab.emite.browser.client;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;

public class DomAssist {

    public DomAssist() {
    }

    public void clearElement(final Element element) {
	final NodeList<Node> childs = element.getChildNodes();
	for (int index = 0, total = childs.getLength(); index < total; index++) {
	    element.removeChild(childs.getItem(index));
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

    public String getMeta(final String name, final boolean isRequired) {
	String value = null;
	final Element element = DOM.getElementById(name);
	if (element != null) {
	    value = element.getPropertyString("content");
	    Log.debug("Meta: " + name + ": " + value);
	}
	if (isRequired && value == null)
	    throw new RuntimeException("Required meta-attribute " + name + " not found.");

	return value;
    }

    public String getMeta(final String name, final String defaultValue) {
	final String value = getMeta(name, false);
	return value != null ? value : defaultValue;
    }

}
