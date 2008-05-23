package com.calclab.emite.client.xep.chatstate;

import java.util.ArrayList;

class ChatStateListenersCollection extends ArrayList<ChatStateListener> implements ChatStateListener {
    private static final long serialVersionUID = 1L;

    public void onActive() {
        for (final ChatStateListener listener : this) {
            listener.onActive();
        }
    }

    public void onComposing() {
        for (final ChatStateListener listener : this) {
            listener.onComposing();
        }
    }

    public void onGone() {
        for (final ChatStateListener listener : this) {
            listener.onGone();
        }
    }

    public void onInactive() {
        for (final ChatStateListener listener : this) {
            listener.onInactive();
        }
    }

    public void onPause() {
        for (final ChatStateListener listener : this) {
            listener.onPause();
        }
    }

}
