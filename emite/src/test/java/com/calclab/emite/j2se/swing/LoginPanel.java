package com.calclab.emite.j2se.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
    public static interface LoginPanelListener {
	void onLogin(String httpBase, String domain, String userName, String password);

	void onLogout();
    }

    private JButton btnLogin;
    private JButton btnLogout;
    private JTextField fieldDomain;
    private JTextField fieldName;
    private JPasswordField fieldPassword;
    private JLabel labelState;
    private final LoginPanelListener listener;
    private JComboBox selectHttpBase;

    public LoginPanel(final LoginPanelListener listener) {
	super(new FlowLayout(FlowLayout.LEFT));
	this.listener = listener;
	init();
    }

    public void showState(final String message, final boolean isConnected) {
	labelState.setText(message + "(connected: " + isConnected + ")");
	btnLogin.setEnabled(!isConnected);
	btnLogout.setEnabled(isConnected);
    }

    private void init() {
	selectHttpBase = new JComboBox();
	selectHttpBase.addItem("http://localhost:8383/http-bind/");
	selectHttpBase.addItem("http://localhost:4444/http-bosh");
	selectHttpBase.addItem("http://localhost:4444/http-bind");
	selectHttpBase.setSelectedIndex(0);
	fieldDomain = new JTextField("localhost");
	fieldName = new JTextField("admin");
	fieldPassword = new JPasswordField("easyeasy");
	add(selectHttpBase);
	add(fieldDomain);
	add(fieldName);
	add(fieldPassword);

	btnLogin = new JButton("login");
	btnLogin.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onLogin(selectHttpBase.getSelectedItem().toString(), fieldDomain.getText(), fieldName
			.getText(), new String(fieldPassword.getPassword()));
	    }
	});
	btnLogout = new JButton("logout");
	btnLogout.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onLogout();
	    }
	});
	add(btnLogin);
	add(btnLogout);

	labelState = new JLabel("current state: none (connected: false)");
	add(labelState);
    }
}
