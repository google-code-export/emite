package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.signal.Slot;

public interface AbstractChatWidget extends EmiteWidget {

    void onSendMessage(Slot<String> slot);

    void setInputEnabled(boolean enabled);

    void setStatus(String status);

    void write(String name, String body);

}
