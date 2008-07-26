package com.calclab.emite.widgets.client.room;

import com.calclab.emite.widgets.client.EmiteWidget;
import com.calclab.suco.client.signal.Signal;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RoomWidget extends DockPanel implements EmiteWidget {

    private final Label status;
    private RoomController controller;
    private final TextArea area;
    private final TextBox input;
    private final Button send;
    Signal<String> onSendMessage;

    public RoomWidget() {
	this.onSendMessage = new Signal<String>("widget:room:sendMessage");
	this.status = new Label();
	this.area = new TextArea();
	this.input = new TextBox();
	this.send = new Button("send", new ClickListener() {
	    public void onClick(final Widget sender) {
		final String text = input.getText();
		if (text.length() > 0)
		    onSendMessage.fire(text);
	    }
	});
	final HorizontalPanel inputBar = new HorizontalPanel();
	inputBar.add(input);
	inputBar.add(send);

	add(status, DockPanel.NORTH);
	add(area, DockPanel.CENTER);
	add(inputBar, DockPanel.SOUTH);
    }

    public String[] getParamNames() {
	return new String[] { "room", "nick" };
    }

    public void setInputEnabled(final boolean enabled) {
	input.setEnabled(enabled);
	send.setEnabled(enabled);
    }

    public void setParam(final String name, final String value) {
	if ("room".equals(name)) {
	    controller.setRoomName(value);
	} else if ("nick".equals(name)) {
	    controller.setNick(value);
	}
    }

    public void setStatus(final String message) {
	status.setText(message);
    }

    public void write(final String from, final String message) {
	final String text = area.getText();
	final String prefix = from != null ? from + ": " : "";
	area.setText(text + prefix + message + "\n");
    }

    void setController(final RoomController controller) {
	this.controller = controller;

    }

}
