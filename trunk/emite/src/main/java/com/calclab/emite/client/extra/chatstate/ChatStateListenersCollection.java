package com.calclab.emite.client.extra.chatstate;

import java.util.ArrayList;

// VICENTE: patrón útil y recomendable (lo hacen en gwt ;) ): una colleción de listeners que implementa el propio interface del listener
// la idea es que la lógica de negocio relacionada con gestionar varios listeners no debería estar en el manager
// PD: por qué utilizas for(int index...) en vez del práctico for(Object : ..)?
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
