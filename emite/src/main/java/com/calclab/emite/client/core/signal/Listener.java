package com.calclab.emite.client.core.signal;

public interface Listener<T> {
    public void onEvent(T parameter);
}
