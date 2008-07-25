package com.calclab.widgets.comenta.client.installer;

import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.bosh3.Connection;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Installer {

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

    public void install(final String id, final Widget widget) {
	clearElement(id);
	RootPanel.get(id).add(widget);
    }

    public void setConnectionSettings(final Connection connection) {
	final Settings boshSettings = getMeta("emite-bosh-", new Settings("httpBase", "host"));
	connection.setSettings(new Bosh3Settings(boshSettings.get("httpBase"), boshSettings.get("host")));

    }

    private void clearElement(final String id) {
	final Element element = DOM.getElementById("emite:widgets:comenta");
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
