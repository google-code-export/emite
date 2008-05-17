package com.calclab.emite.client.extra.messageevent;

import com.calclab.emite.client.im.chat.Chat;

public interface ChatStatusListener {

    void onActive(Chat chat);

    void onComposing(Chat chat);

    void onGone(Chat chat);

    void onInactive(Chat chat);

    void onPause(Chat chat);

}
