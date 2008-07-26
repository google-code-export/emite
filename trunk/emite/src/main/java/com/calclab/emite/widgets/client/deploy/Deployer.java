package com.calclab.emite.widgets.client.deploy;

import java.util.ArrayList;

import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.suco.client.container.Container;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Deployer {

    public void deploy(final String divClass, final Class<? extends Widget> widgetClass, final Container container) {
	final ArrayList<Element> elements = findElementsByClass(divClass);
	for (final Element e : elements) {
	    install(e, container.getInstance(widgetClass));
	}
    }

    public ArrayList<Element> findElementsByClass(final String divClass) {
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

    public void install(final Element element, final Widget widget) {
	clearElement(element);
	RootPanel.get(element.getId()).add(widget);
    }

    public void setConnectionSettings(final Connection connection) {
	final Settings boshSettings = getMeta("emite.bosh.", new Settings("httpBase", "host"));
	connection.setSettings(new Bosh3Settings(boshSettings.get("httpBase"), boshSettings.get("host")));

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
	return DOM.getElementProperty(DOM.getElementById(prefix + key), "content");
    }
}
