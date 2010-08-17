package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.logic.PingChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingSessionPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongSessionPresenter;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class PingPongExamplesEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	PingPongDisplay display = new PingPongChatWidget();
	RootPanel.get().add(display.asWidget());
	new PingPongCommonPresenter(display);

	XmppURI other = XmppURI.uri(PageAssist.getMeta("pingpong.other"));
	final String clientType = PageAssist.getMeta("pingpong.type");
	display.print("Ping pong example type: " + clientType, Style.info);
	if ("ping".equals(clientType)) {
	    new PingSessionPresenter(other, display).start();
	} else if ("pong".equals(clientType)) {
	    new PongSessionPresenter(other, display).start();
	} else if ("pingChat".equals(clientType)) {
	    new PingChatPresenter(other, display).start();
	} else if ("pongChat".equals(clientType)) {
	    new PongChatPresenter(display).start();
	} else if ("pingRoom".equals(clientType)) {
	    new PingRoomPresenter(other, display).start();
	} else if ("pongRoom".equals(clientType)) {
	    new PongRoomPresenter(other, display).start();
	} else {
	    display.printHeader("You need to configure the pingpong.type meta tag!! "
		    + " (possible values: ping, pong, pingChat, pongChat, pingRoom, pongRoom)",
		    PingPongDisplay.Style.error);
	}
    }
}
