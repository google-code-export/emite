package com.calclab.uimite.client.chat;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.Message;
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

    /**
     * Esto es una manera de dividir una clase en vez de tener que escribir
     * <code>chatView.addTop()</code> escribimos
     * <code>chatView.extensions.addTop()</code> que es un poco más
     * explíticito en cuanto a las intenciones. también nos permite saber de una
     * manera rápida (mirando los métodos de la clase chatView.extensions) en
     * qué es susceptible de ser extendido... ¿me explico? ¿qué te parece?
     * 
     * @author dani
     * 
     */
    public class ChatViewExtensions {
	public void addTop(final UIView view) {
	    ChatView.this.add((Widget) view, DockPanel.NORTH);
	}
    }

    public static interface ChatViewListener {

	void onContentChanged();

    }

    public final ChatViewExtensions extensions;
    private final VerticalPanel output;
    private final TextBox inputTextBox;

    public ChatView(final Chat chat, final ChatViewListener listener) {
	this.extensions = new ChatViewExtensions();
	output = new VerticalPanel();
	add(output, DockPanel.CENTER);
	final HorizontalPanel input = new HorizontalPanel();
	inputTextBox = new TextBox();
	input.add(inputTextBox);
	final Button buttonSend = new Button("send", new ClickListener() {
	    public void onClick(final Widget sender) {
		chat.send(inputTextBox.getText());
		inputTextBox.setText("");

	    }
	});
	input.add(buttonSend);
	add(input, DockPanel.SOUTH);

	chat.addListener(new ChatListener() {
	    public void onMessageReceived(final Chat chat, final Message message) {
		show(message.getFromURI().toString(), message.getBody());
		listener.onContentChanged();
	    }

	    public void onMessageSent(final Chat chat, final Message message) {
		show("me", message.getBody());
	    }

	});
    }

    protected void show(final String name, final String body) {
	output.add(new Label(name + ": " + body));
    }

}
