package com.calclab.emite.client.extra.messageevent;

import com.calclab.emite.client.im.chat.Chat;

public interface MessageEventListener {

    void onComposing(Chat chat);

    void onDelivered(Chat chat);

    void onDisplayed(Chat chat);

    void onOffline(Chat chat);

}
