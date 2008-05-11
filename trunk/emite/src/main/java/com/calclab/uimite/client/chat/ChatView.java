package com.calclab.uimite.client.chat;

import java.util.ArrayList;

import com.calclab.uimite.client.UIView;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatView extends DockPanel implements UIView {

    public static interface ChatViewListener {
	void onContentChanged();

	void onSend(String text);
    }

    @SuppressWarnings("serial")
    public static class ChatViewListenerCollection extends ArrayList<ChatViewListener> implements ChatViewListener {
	public void onContentChanged() {
	    for (final ChatViewListener listener : this) {
		listener.onContentChanged();
	    }

	}

	public void onSend(final String text) {
	    for (final ChatViewListener listener : this) {
		listener.onSend(text);
	    }
	}
    }

    private final VerticalPanel output;
    private final TextBox inputTextBox;
    private final ChatViewListenerCollection listeners;

    public ChatView() {
	this.listeners = new ChatViewListenerCollection();
	output = new VerticalPanel();
	add(output, DockPanel.CENTER);
	final HorizontalPanel input = new HorizontalPanel();
	inputTextBox = new TextBox();
	input.add(inputTextBox);
	final Button buttonSend = new Button("send", new ClickListener() {
	    public void onClick(final Widget sender) {
		listeners.onSend(inputTextBox.getText());
		inputTextBox.setText("");

	    }
	});
	input.add(buttonSend);
	add(input, DockPanel.SOUTH);
    }

    public void addListener(final ChatViewListener listener) {
	listeners.add(listener);
    }

    protected void show(final String name, final String body) {
	output.add(new Label(name + ": " + body));
	listeners.onContentChanged();
    }

}
