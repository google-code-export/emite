package com.calclab.emiteuimodule.client.chat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class ChatUIPresenterTest {

    private ChatUIPresenter chatUI;
    private ChatUIView view;
    private XmppURI otherUri;

    @Before
    public void begin() {
	otherUri = XmppURI.uri("someone", "example.com", "home");
	chatUI = new ChatUIPresenter(otherUri, "luther", "black");
	view = Mockito.mock(ChatUIView.class);
	chatUI.init(view);
	Mockito.verify(view, Mockito.times(1)).setChatTitle(otherUri.getNode(), otherUri.toString());
    }

    @Test
    public void onActiveSomeMessageDontHightlight() {
	chatUI.onActivated();
	sendSomeMessage();
	verifyUnHightLightTimes(1);
	verifyHightLightTimes(0);
    }

    @Test
    public void onDeactivateSomeMessagesActivateOnlyOneHightlight() {
	chatUI.onDeactivated();
	sendSomeMessage();
	sendSomeMessage();
	sendSomeMessage();
	chatUI.onActivated();
	verifyUnHightLightTimes(2);
	verifyHightLightTimes(1);
    }

    @Test
    public void onSomeMessageInfoHightlight() {
	chatUI.onDeactivated();
	chatUI.addInfoMessage("some info");
	verifyUnHightLightTimes(1);
	verifyHightLightTimes(1);
    }

    @Test
    public void onSomeMessagesOnlyOneHightlight() {
	chatUI.onDeactivated();
	sendSomeMessage();
	sendSomeMessage();
	sendSomeMessage();
	verifyUnHightLightTimes(1);
	verifyHightLightTimes(1);
    }

    private void sendSomeMessage() {
	chatUI.addMessage("otheruser", "hello world");
    }

    private void verifyHightLightTimes(final int times) {
	Mockito.verify(view, Mockito.times(times)).setChatIconCls("chat-h-icon");
    }

    private void verifyUnHightLightTimes(final int times) {
	Mockito.verify(view, Mockito.times(times)).setChatIconCls("chat-icon");
    }
}
