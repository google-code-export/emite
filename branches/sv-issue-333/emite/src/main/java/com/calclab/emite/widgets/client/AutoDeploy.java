/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.browser.client.AutoConfig;
import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.ioc.Provider;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class AutoDeploy {
    private final DomAssist domAssist;
    private final WidgetsRegistry registry;

    public AutoDeploy(final WidgetsRegistry registry, final AutoConfig controller, final DomAssist domAssist) {
	this.registry = registry;
	this.domAssist = domAssist;

	deployWidgets();
    }

    public void deploy(final String divClass, final Provider<? extends EmiteWidget> provider) {
	final ArrayList<Element> elements = domAssist.findElementsByClass(divClass);
	for (final Element e : elements) {
	    install(e, provider.get());
	}
    }

    public void install(final Element element, final EmiteWidget widget) {
	final String elementID = element.getId();
	if (elementID == null) {
	    Log.error("Trying to install a widget in a element without a id:" + element);
	}
	Log.debug("Installing on element id: " + elementID);
	setParams(element, widget);
	domAssist.clearElement(element);
	RootPanel.get(elementID).add((Widget) widget);
    }

    private void deployWidgets() {
	for (final String divClass : registry.getClasses()) {
	    final Provider<? extends EmiteWidget> provider = registry.getProvider(divClass);
	    deploy(divClass, provider);
	}
    }

    private void setParams(final Element element, final EmiteWidget widget) {
	final String[] paramNames = widget.getParamNames();
	if (paramNames != null) {
	    for (final String name : paramNames) {
		Log.debug("Param name of widget: " + name);
		final String value = element.getAttribute("data-" + name);
		Log.debug("Value: " + value);
		widget.setParam(name, value);
	    }
	}
    }

}
