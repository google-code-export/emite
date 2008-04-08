package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
    public static interface ChatPanelListener {
	void onSend(ChatPanel source, String text);
    }

    private final JTextArea area;
    private final JTextField fieldBody;

    public ChatPanel(final ChatPanelListener listener) {
	super(new BorderLayout());
	this.area = new JTextArea();
	add(new JScrollPane(area));

	final ActionListener actionListener = new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onSend(ChatPanel.this, fieldBody.getText());
	    }
	};

	fieldBody = new JTextField();
	final JButton btnSend = new JButton("send");
	fieldBody.addActionListener(actionListener);
	btnSend.addActionListener(actionListener);
	final JPanel panel = new JPanel(new BorderLayout());
	panel.add(fieldBody);
	panel.add(btnSend, BorderLayout.EAST);
	add(panel, BorderLayout.SOUTH);

    }

    public void clearMessage() {
	fieldBody.setText("");
	fieldBody.requestFocus();
    }

    public void showIcomingMessage(final String from, final String body) {
	print(from + ": " + body);
    }

    public void showOutMessage(final String body) {
	print("me: " + body);
    }

    private void print(final String message) {
	area.setText(area.getText() + "\n" + message);
    }

}
