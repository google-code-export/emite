package com.calclab.emite.client.core.signals;

public interface Listener<T> {
    public void onEvent(T parameter);
}
