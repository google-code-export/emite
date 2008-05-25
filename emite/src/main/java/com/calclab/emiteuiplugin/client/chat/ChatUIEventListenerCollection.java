package com.calclab.emiteuiplugin.client.chat;

import java.util.ArrayList;

public class ChatUIEventListenerCollection extends ArrayList<ChatUIEventListener> implements ChatUIEventListener {
    private static final long serialVersionUID = 1L;

    public void onClose() {
        for (final ChatUIEventListener listener : this) {
            listener.onClose();
        }
    }

    public void onComposing() {
        for (final ChatUIEventListener listener : this) {
            listener.onComposing();
        }
    }

    public void onInputFocus() {
        for (final ChatUIEventListener listener : this) {
            listener.onInputFocus();
        }
    }

    public void onInputUnFocus() {
        for (final ChatUIEventListener listener : this) {
            listener.onInputUnFocus();
        }
    }

}
