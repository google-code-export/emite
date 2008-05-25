package com.calclab.emiteuiplugin.client.chat;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.xep.chatstate.ChatState;
import com.calclab.emite.client.xep.chatstate.ChatStateListener;
import com.calclab.emite.client.xep.chatstate.ChatState.Type;

public class ChatStatePresenter {

    private static final int CANCEL_TIMER = -1;
    private static final int MILLISECONS_TO_PAUSE = 5000;
    private static final int MILLISECONS_TO_INACTIVE = 30000;

    public ChatState chatState;
    private final ChatStateTimer timer;

    public ChatStatePresenter(final I18nTranslationService i18n, final ChatState chatState, final ChatUI chatUI) {
        this.chatState = chatState;
        timer = new ChatStateTimer(this);
        chatUI.addEventListener(new ChatUIEventListener() {
            public void onClose() {
                setOwnState(ChatState.Type.gone);
            }

            public void onComposing() {
                setOwnState(ChatState.Type.composing);
            }

            public void onInputFocus() {
                setOwnState(ChatState.Type.active);
            }

            public void onInputUnFocus() {
                setOwnState(ChatState.Type.inactive);
            }
        });

        chatState.addOtherStateListener(new ChatStateListener() {
            String otherAlias = chatUI.getOtherAlias();

            public void onActive() {
                chatUI.setSavedChatNotification(new ChatNotification());
                chatUI.clearMessageEventInfo();
            }

            public void onComposing() {
                chatUI.setSavedChatNotification(formatNotification(otherAlias, "is writing", "e-notif-composing"));
                chatUI.showMessageEventInfo();
            }

            public void onGone() {
                chatUI.setSavedChatNotification(formatNotification(otherAlias, "finished the conversation",
                        "e-notif-gone"));
                chatUI.showMessageEventInfo();
            }

            public void onInactive() {
                // FIXME: this kind of messages only for tests ...
                chatUI.setSavedChatNotification(formatNotification(otherAlias, "is inactive", "e-notif-inactive"));
                chatUI.showMessageEventInfo();
            }

            public void onPause() {
                chatUI.setSavedChatNotification(formatNotification(otherAlias, "stop to write", "e-notif-pause"));
                chatUI.showMessageEventInfo();
            }

            private ChatNotification formatNotification(final String otherAlias, final String text, final String style) {
                return new ChatNotification(i18n.t("[%s] " + text, otherAlias), style);
            }

        });
    }

    protected void onTime() {
        switch (chatState.getOwnState()) {
        case composing:
            setOwnState(ChatState.Type.pause);
            break;
        case active:
            setOwnState(ChatState.Type.inactive);
            break;
        case pause:
            setOwnState(ChatState.Type.inactive);
            break;
        default:
            timer.cancel();
            break;
        }
    }

    private void setOwnState(final Type type) {
        if (chatState != null && chatState.getNegotiationStatus().equals(ChatState.NegotiationStatus.accepted)) {
            int time = CANCEL_TIMER;
            switch (type) {
            case composing:
                time = MILLISECONS_TO_PAUSE;
                break;
            case active:
                time = MILLISECONS_TO_INACTIVE;
                break;
            case inactive:
                time = CANCEL_TIMER;
                break;
            case gone:
                time = CANCEL_TIMER;
                break;
            case pause:
                time = MILLISECONS_TO_INACTIVE;
                break;
            }
            if (time > 0) {
                timer.schedule(time);
            } else {
                timer.cancel();
            }
            chatState.setOwnState(type);
        }
    }
}
