package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

public abstract class AbstractPresenter {

    public void doAction(final String action, final Object value) {
        DefaultDispatcher.getInstance().fire(action, value);
    }
}