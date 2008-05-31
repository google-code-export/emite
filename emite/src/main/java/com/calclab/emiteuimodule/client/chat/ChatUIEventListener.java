package com.calclab.emiteuimodule.client.chat;

public interface ChatUIEventListener {

    void onClose();

    void onComposing();

    void onInputFocus();

    void onInputUnFocus();

}
