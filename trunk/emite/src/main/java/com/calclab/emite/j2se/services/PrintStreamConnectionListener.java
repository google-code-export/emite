package com.calclab.emite.j2se.services;

import java.io.PrintStream;

public class PrintStreamConnectionListener implements HttpConnectorListener {

    private final PrintStream out;

    public PrintStreamConnectionListener() {
	this(System.out);
    }

    public PrintStreamConnectionListener(final PrintStream out) {
	this.out = out;
    }

    public void onError(final String id, final String cause) {
	out.println("CONN # " + id + "-ERROR: " + cause);
    }

    public void onFinish(final String id, final long duration) {
	out.println("CONN FINISHED: " + id + " with duration: " + duration);
    }

    public void onResponse(final String id, final String response) {
	out.println("CONN IN: " + response);
    }

    public void onSend(final String id, final String xml) {
	out.println("CONN OUT: " + xml);
    }

    public void onStart(final String id) {
	out.println("CONN START: " + id);
    }

}
