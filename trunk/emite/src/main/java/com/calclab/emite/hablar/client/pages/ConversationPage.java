package com.calclab.emite.hablar.client.pages;

import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationPage extends DockPanel {
    private Listener0 onClose;
    private final VerticalPanel output;
    private final Event<String> onSendMessage;

    public ConversationPage() {
	onSendMessage = new Event<String>("conversationView:onSendMessage");
	add(createToolBar(), DockPanel.NORTH);

	output = new VerticalPanel();
	add(output, DockPanel.CENTER);

	final FlowPanel actions = new FlowPanel();
	actions.addStyleName("actions");
	final TextBox input = new TextBox();
	actions.add(input);
	final Button btnSend = new Button("send", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onSendMessage.fire(input.getText());
		input.setText("");
		input.setFocus(true);
	    }
	});
	actions.add(btnSend);
	add(actions, DockPanel.SOUTH);
    }

    public void onClose(final Listener0 onClose) {
	this.onClose = onClose;
    }

    public void onSendMessage(final Listener<String> listener) {
	onSendMessage.add(listener);
    }

    public void writeln(final String name, final String body) {
	output.add(new HTML("<b>" + name + ":</b> " + body));
    }

    private FlowPanel createToolBar() {
	final FlowPanel toolbar = new FlowPanel();
	toolbar.addStyleName("toolbar");
	final Button btnClose = new Button("close", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onClose.onEvent();
	    }
	});
	toolbar.add(btnClose);
	return toolbar;
    }

}
