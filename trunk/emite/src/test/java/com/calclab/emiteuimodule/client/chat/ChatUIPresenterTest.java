package com.calclab.emiteuimodule.client.chat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.roster.ChatIconDescriptor;

public class ChatUIPresenterTest {

    private ChatUIPresenter chatUI;
    private ChatUIView view;
    private XmppURI otherUri;

    @Before
    public void begin() {
        ChatUIListener listener = Mockito.mock(ChatUIListener.class);
        otherUri = new XmppURI("someone", "example.com", "home");
        chatUI = new ChatUIPresenter(otherUri, "luther", "black", listener);
        view = Mockito.mock(ChatUIView.class);
        chatUI.init(view);
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
        Mockito.verify(view, Mockito.times(times)).setChatTitle(otherUri.getNode(), otherUri.toString(),
                ChatIconDescriptor.chatnewmessagesmall);
    }

    private void verifyUnHightLightTimes(final int times) {
        Mockito.verify(view, Mockito.times(times)).setChatTitle(otherUri.getNode(), otherUri.toString(),
                ChatIconDescriptor.chatsmall);
    }
}
