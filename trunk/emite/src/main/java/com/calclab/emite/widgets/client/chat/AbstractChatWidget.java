package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.base.DockableWidget;
import com.calclab.suco.client.listener.Listener;

public interface AbstractChatWidget extends EmiteWidget, DockableWidget {

    public AbstractChatController getController();

    public void setChat(Chat chat);

    public void setController(AbstractChatController chatController);

    void onSendMessage(Listener<String> slot);

    void setInputEnabled(boolean enabled);

    void write(String name, String body);

}
