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
package com.calclab.emite.widgets.habla.client;

import java.util.ArrayList;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.suco.client.ioc.Provider;
import com.google.gwt.dom.client.Element;
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
	    widget.setProperties(domAssist.getProperties(element, widget.getPropertyNames(), "data-"));
	    RootPanel.get(element.getId()).add(widget);
	}
    }

}
