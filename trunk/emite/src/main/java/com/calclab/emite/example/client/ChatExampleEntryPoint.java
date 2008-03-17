package com.calclab.emite.example.client;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connection.ConnectionListener;
import com.calclab.emite.client.im.MessageListener;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.packet.stanza.Message;
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

public class ChatExampleEntryPoint implements EntryPoint {
	private Button btnLogin;
	private Button btnLogout;
	private TextBox messageIn;
	private TextArea messageOutput;
	private TextArea out;
	private PasswordTextBox passwordInput;
	private TextBox toIn;
	private TextBox userNameInput;
	private ListBox userSelector;
	private Xmpp xmpp;

	public void onModuleLoad() {

		this.xmpp = Xmpp.create(new BoshOptions("http-bind", "localhost"), new LoggerOutput() {
			public void log(final int level, final String message) {
				print("[LOGGER - " + level + " ]\n" + message);
			}
		});

		xmpp.addConnectionListener(new ConnectionListener() {
			public void onConnected() {
				print("CONNECTION - connected");
			}

			public void onConnecting() {
				print("CONNECTION - connecting");
			}

			public void onDisconnected() {
				print("CONNECTION - disconnected");
			}
		});

		xmpp.getRoster().addListener(new RosterListener() {

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
		btnLogin = new Button("Login", new ClickListener() {
			public void onClick(final Widget source) {
				xmpp.login(userNameInput.getText(), passwordInput.getText());
				btnLogin.setEnabled(false);
				btnLogout.setEnabled(true);
			}
		});
		buttons.add(btnLogin);
		btnLogout = new Button("Logout", new ClickListener() {
			public void onClick(final Widget arg0) {
				xmpp.logout();
				btnLogout.setEnabled(true);
				btnLogin.setEnabled(true);
			}
		});
		buttons.add(btnLogout);

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
		vertical.add(createMessagePane());
		vertical.add(createOutputPane());

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
		this.out = new TextArea();
		split.add(out);
		return split;
	}

}
