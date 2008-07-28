package com.calclab.emite.widgets.client;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.browser.DomAssist;
import com.calclab.emite.client.browser.PageController;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.chat.CharlaWidget;
import com.calclab.emite.widgets.client.chat.GWTChatWidget;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.logout.LogoutWidget;
import com.calclab.emite.widgets.client.room.ComentaWidget;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.suco.client.container.Container;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class AutoDeploy {
    private final DomAssist domAssist;
    private final Container container;

    public AutoDeploy(final Container container, final PageController controller, final DomAssist domAssist) {
	this.container = container;
	this.domAssist = domAssist;

	controller.configureConnection();
	controller.configureCloseAction();
	deployWidgets();
	controller.resumeSession();
    }

    public void deploy(final String divClass, final Class<? extends EmiteWidget> widgetClass) {
	final ArrayList<Element> elements = domAssist.findElementsByClass(divClass);
	for (final Element e : elements) {
	    install(e, container.getInstance(widgetClass));
	}
    }

    public void install(final Element element, final EmiteWidget widget) {
	Log.debug("Installing on element id: " + element.getId());
	setParams(element, widget);
	domAssist.clearElement(element);
	RootPanel.get(element.getId()).add((Widget) widget);
    }

    private void deployWidgets() {
	deploy("emite-widget-charla", CharlaWidget.class);
	deploy("emite-widget-comenta", ComentaWidget.class);
	deploy("emite-widget-logger", LoggerWidget.class);
	deploy("emite-widget-login", LoginWidget.class);
	deploy("emite-widget-chat", GWTChatWidget.class);
	deploy("emite-widget-room", RoomWidget.class);
	deploy("emite-widget-room-presence", RoomPresenceWidget.class);
	deploy("emite-widget-logout", LogoutWidget.class);
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
