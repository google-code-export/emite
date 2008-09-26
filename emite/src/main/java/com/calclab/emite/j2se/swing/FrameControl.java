package com.calclab.emite.j2se.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.calclab.emite.core.client.xmpp.session.Session;

public class FrameControl {

    public FrameControl(final Session session, final ClientPanel clientPanel, final JFrame frame) {
	frame.setContentPane(clientPanel);
	frame.setSize(900, 400);
	frame.setVisible(true);
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(final WindowEvent e) {
		session.logout();
		System.exit(0);
	    }
	});
    }

}
