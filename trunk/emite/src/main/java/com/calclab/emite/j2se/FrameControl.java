package com.calclab.emite.j2se;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.calclab.emite.core.client.xmpp.session.Session;

public class FrameControl {

    private final Session session;

    public FrameControl(final Session session) {
	this.session = session;
    }

    public void setView(final JFrame frame) {
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
