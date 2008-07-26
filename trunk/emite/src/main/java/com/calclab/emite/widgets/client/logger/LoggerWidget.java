package com.calclab.emite.widgets.client.logger;

import com.calclab.emite.widgets.client.EmiteWidget;
import com.calclab.suco.client.signal.Signal0;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoggerWidget extends DockPanel implements EmiteWidget {
    public static final String ERROR = "error";
    public static final String SENT = "sent";
    public static final String RECEIVED = "received";
    private final VerticalPanel content;
    Signal0 onClear;
    private final ScrollPanel scroll;

    public LoggerWidget() {
	this.onClear = new Signal0("widgets.logger:onClear");
	content = new VerticalPanel();
	final Button clear = new Button("clear", new ClickListener() {
	    public void onClick(final Widget sender) {
		onClear.fire();
	    }
	});
	final FlowPanel panel = new FlowPanel();
	panel.add(clear);
	add(panel, DockPanel.SOUTH);

	scroll = new ScrollPanel();
	scroll.addStyleName("content");
	scroll.setAlwaysShowScrollBars(true);
	scroll.add(content);
	add(scroll, DockPanel.CENTER);
    }

    public void clearContent() {
	content.clear();
    }

    public String[] getParamNames() {
	return new String[] {};
    }

    public void setParam(final String name, final String value) {
    }

    public void write(final String color, final String message) {
	final Label label = new Label(message);
	label.addStyleName(color);
	content.add(label);
	scroll.ensureVisible(label);
    }

}
