package com.calclab.emite.browser.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;

/**
 * A helper class to perform typical dom manipulations related with widgets
 * 
 */
public class DomAssist {

    public DomAssist() {
    }

    /**
     * Remove all the childs of the given element
     * 
     * @param element
     *            the element
     */
    public void clearElement(final Element element) {
	final NodeList<Node> childs = element.getChildNodes();
	for (int index = 0, total = childs.getLength(); index < total; index++) {
	    element.removeChild(childs.getItem(index));
	}

    }

    /**
     * Retrieves all the div on the page that matches the given class
     * 
     * @param cssClass
     *            the css class to match
     * @return a list of elements
     */
    public ArrayList<Element> findElementsByClass(final String cssClass) {
	Log.debug("Finding elements div." + cssClass);
	final ArrayList<Element> elements = new ArrayList<Element>();
	final Document document = Document.get();
	final NodeList<Element> divs = document.getElementsByTagName("div");
	for (int index = 0, total = divs.getLength(); index < total; index++) {
	    final Element div = divs.getItem(index);
	    final String[] classes = div.getClassName().split(" ");
	    for (final String className : classes) {
		if (cssClass.equals(className)) {
		    elements.add(div);
		}
	    }
	}
	return elements;
    }

    /**
     * Get the value of meta information writen in the html page. The meta
     * information is a html tag with name of meta usually placed inside the the
     * head section with two attributes: id and content. For example:
     * 
     * <code>&lt;meta id="name" value="userName" /&gt;</code>
     * 
     * 
     * 
     * @param id
     *            the 'id' value of the desired meta tag
     * @param isRequired
     *            if isRequired is true, this method throws a RuntimeException
     *            if the meta tag is not found
     * @return the value of the attribute 'value'
     */
    public String getMeta(final String id, final boolean isRequired) {
	String value = null;
	final Element element = DOM.getElementById(id);
	if (element != null) {
	    value = element.getPropertyString("content");
	    Log.debug("Meta: " + id + ": " + value);
	}
	if (isRequired && value == null)
	    throw new RuntimeException("Required meta-attribute " + id + " not found.");

	return value;
    }

    /**
     * The same as getMeta(string, boolean) but returns the defaultValue if no
     * meta is found
     * 
     * @param id
     *            the 'id' value of the desired meta tag
     * @param defaultValue
     *            the default value to be return if no meta found
     * @return the value of the 'value' attribute or defaultValue if not found
     */
    public String getMeta(final String id, final String defaultValue) {
	final String value = getMeta(id, false);
	return value != null ? value : defaultValue;
    }

    /**
     * Ask the widget for all the property names and get the values from the
     * element. Always call the widget.setProperties method
     * 
     * Given the properties [name, color] the element should be:
     * <code>&lt;div data-name="name value" data-color="color value" /&gt;</code>
     * 
     * @param element
     * @param widget
     */
    public void setProperties(final Element element, final HasProperties widget) {
	final String[] paramNames = widget.getPropertyNames();
	final HashMap<String, String> properties = new HashMap<String, String>();
	if (paramNames != null) {
	    for (final String name : paramNames) {
		Log.debug("Param name of widget: " + name);
		final String value = element.getAttribute("data-" + name);
		Log.debug("Value: " + value);
		properties.put(name, value);
	    }
	}
	widget.setProperties(properties);
    }

}
