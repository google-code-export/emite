package com.calclab.emiteuiplugin.client.chat;

public interface ChatUIEventListener {

    void onClose();

    void onComposing();

    void onInputFocus();

    void onInputUnFocus();

}
