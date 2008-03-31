package com.calclab.emite.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.calclab.emite.client.connector.HttpConnectorListener;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.x.im.chat.MessageListener;
import com.calclab.emite.client.x.im.session.SessionListener;
import com.calclab.emite.client.x.im.session.Session.State;

public class SwingClient {

	public static void main(final String args[]) {
		new SwingClient().start();
	}

	private final JTextArea area;
	private JButton btnLogin;
	private JButton btnLogout;
	private JButton btnPanic;
	private JButton btnSend;
	private JTextField fieldDomain;
	private JTextField fieldHttpBase;
	private JTextField fieldJID;
	private JTextField fieldMessage;
	private JTextField fieldName;
	private JPasswordField fieldPassword;
	private final JPanel messager;
	private final JPanel root;
	private Xmpp xmpp;

	public SwingClient() {
		root = new JPanel(new BorderLayout());

		root.add(createLoginPanel(), BorderLayout.NORTH);
		area = new JTextArea();
		area.setWrapStyleWord(true);
		root.add(new JScrollPane(area), BorderLayout.CENTER);
		messager = createMessager();
		root.add(messager, BorderLayout.SOUTH);

		initXMPP();
	}

	protected void print(final String message) {
		print(message, area);
	}

	private JPanel createLoginPanel() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnLogin = new JButton("login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				xmpp.login(fieldName.getText(), new String(fieldPassword.getPassword()));
			}
		});
		btnLogout = new JButton("logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				xmpp.logout();
			}
		});
		btnPanic = new JButton("panic");
		btnPanic.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				xmpp.getDispatcher().publish(Connection.Events.error);
			}
		});
		final JButton btnClear = new JButton("clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				area.setText("");
			}
		});

		fieldHttpBase = new JTextField("http://localhost:8181/http-bind/");
		fieldDomain = new JTextField("localhost");
		fieldName = new JTextField("admin");
		fieldPassword = new JPasswordField("easyeasy");

		panel.add(fieldHttpBase);
		panel.add(fieldDomain);
		panel.add(fieldName);
		panel.add(fieldPassword);
		panel.add(btnLogin);
		panel.add(btnLogout);
		panel.add(btnPanic);
		panel.add(btnClear);
		return panel;
	}

	private JPanel createMessager() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.fieldJID = new JTextField("testuser1@localhost");
		this.fieldMessage = new JTextField("this is the body message");
		this.btnSend = new JButton("send!");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final String to = fieldJID.getText();
				final String body = fieldMessage.getText();
				xmpp.send(to, body);
				print("me: (" + to + "): " + body);
			}
		});

		panel.add(fieldJID);
		panel.add(fieldMessage);
		panel.add(btnSend);
		return panel;
	}

	private void initXMPP() {
		this.xmpp = TestHelper.createXMPP(new HttpConnectorListener() {
			public void onError(final String id, final String cause) {
				print((id + "-ERROR: " + cause));
			}

			public void onFinish(final String id, final long duration) {
				print((id + "-FINISH " + duration + "ms"));
			}

			public void onResponse(final String id, final String response) {
				print((id + "-RESPONSE: " + response));
			}

			public void onSend(final String id, final String xml) {
				print((id + "-SENDING: " + xml));
			}

			public void onStart(final String id) {
				print((id + "-STARTED: " + id));
			}

		});
		xmpp.addSessionListener(new SessionListener() {
			public void onStateChanged(final State old, final State current) {
				print("STATE: " + current);
				switch (current) {
				case disconnected:
					setConnectedState(false);
				case connected:
					setConnectedState(true);
				}
			}
		});
		xmpp.addMessageListener(new MessageListener() {
			public void onReceived(final Message message) {
				print(message.getFrom() + ": " + message.getBody());
			}
		});
	}

	private void print(final String message, final JTextArea out) {
		out.setText(out.getText() + "\n" + message);
	}

	private void setConnectedState(final boolean isConnected) {
		// btnLogin.setEnabled(isConnected);
		// btnLogout.setEnabled(!isConnected);
	}

	private void start() {
		final JFrame frame = new JFrame("emite swing client");
		frame.setContentPane(root);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		frame.setVisible(true);
	}
}
