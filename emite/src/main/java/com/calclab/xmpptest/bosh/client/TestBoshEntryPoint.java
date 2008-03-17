package com.calclab.xmpptest.bosh.client;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.bosh.Bosh;
import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.im.MessageListener;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.xmpptest.bosh.client.script.Interpreter;
import com.calclab.xmpptest.bosh.client.script.Output;
import com.calclab.xmpptest.bosh.client.script.Parser;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestBoshEntryPoint implements EntryPoint, Output {
	private Bosh bosh;
	private TextArea in;
	private Interpreter interpreter;
	private TextBox messageIn;
	private TextArea messageOutput;
	private TextArea out;
	private PasswordTextBox passwordInput;
	private TextBox toIn;
	private TextBox userNameInput;
	private ListBox userSelector;
	private Xmpp xmpp;

	public void onModuleLoad() {
		this.in = new TextArea();
		this.out = new TextArea();

		this.interpreter = new Interpreter(this);

		this.xmpp = Xmpp.create(new BoshOptions("http-bind", "localhost"), new LoggerOutput() {
			public void log(final int level, final String message) {
				print("[LOGGER - " + level + " ]\n" + message);
			}
		});

		xmpp.addMessageListener(new MessageListener() {
			public void onReceived(final Message message) {
				String text = messageOutput.getText();
				text += "\nIN [" + message.getFrom() + "]\n";
				text += message.getBody();
				messageOutput.setText(text);
			}
		});

		createInterface();
	}

	public void print(final String text) {
		out.setText(out.getText() + text + "\n");
	}

	private HorizontalPanel createButtonsPane() {
		final HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(new Button("Start", new ClickListener() {
			public void onClick(final Widget source) {
				bosh.start();
			}

		}));
		buttons.add(new Button("Stop", new ClickListener() {
			public void onClick(final Widget source) {
				bosh.stop();
			}

		}));

		buttons.add(new Button("Script!", new ClickListener() {

			public void onClick(final Widget source) {
				interpreter.run(Parser.getScript(in.getText()));
			}

		}));

		buttons.add(new Button("Xmpp", new ClickListener() {
			public void onClick(final Widget source) {
				xmpp.login(userNameInput.getText(), passwordInput.getText());
			}
		}));

		buttons.add(new Button("Clear", new ClickListener() {
			public void onClick(final Widget source) {
				out.setText("");
			}
		}));
		return buttons;
	}

	private void createInterface() {
		final VerticalPanel vertical = new VerticalPanel();
		vertical.add(createButtonsPane());
		vertical.add(createLoginPane());
		vertical.add(createOutputPane());
		vertical.add(createMessagePane());

		RootPanel.get().add(vertical);
	}

	private HorizontalPanel createLoginPane() {
		final HorizontalPanel login = new HorizontalPanel();
		userNameInput = new TextBox();
		passwordInput = new PasswordTextBox();
		login.add(new Label("user name:"));
		login.add(userNameInput);
		login.add(new Label("password"));
		login.add(passwordInput);
		return login;
	}

	private VerticalPanel createMessagePane() {
		final VerticalPanel pane = new VerticalPanel();

		final HorizontalPanel controls = new HorizontalPanel();
		pane.add(controls);

		userSelector = new ListBox(false);
		controls.add(userSelector);
		toIn = new TextBox();
		controls.add(toIn);
		messageIn = new TextBox();
		controls.add(messageIn);
		final Button btnSend = new Button("send", new ClickListener() {
			public void onClick(Widget arg0) {
				String msg = messageIn.getText();
				messageIn.setText("");
				messageOutput.setText(messageOutput.getText() + "\n" + "sending: " + msg);
				xmpp.send(toIn.getText(), msg);
			}
		});
		controls.add(btnSend);

		messageOutput = new TextArea();
		pane.add(messageOutput);

		return pane;
	}

	private HorizontalPanel createOutputPane() {
		final HorizontalPanel split = new HorizontalPanel();
		split.add(in);
		split.add(out);
		return split;
	}

}
