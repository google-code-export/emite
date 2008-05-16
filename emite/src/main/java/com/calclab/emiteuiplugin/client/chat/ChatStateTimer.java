package com.calclab.emiteuiplugin.client.chat;

import com.google.gwt.user.client.Timer;

public class ChatStateTimer {
    private final Timer timer;

    public ChatStateTimer(final ChatUIPresenter presenter) {
        timer = new Timer() {
            @Override
            public void run() {
                presenter.onTime();
            }
        };
    }

    public void cancel() {
        timer.cancel();
    }

    public void schedule(final int delayMillis) {
        timer.schedule(delayMillis);
    }

}
