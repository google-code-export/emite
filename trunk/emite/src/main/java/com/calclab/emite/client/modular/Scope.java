package com.calclab.emite.client.modular;

public interface Scope {
    <T> Provider<T> scope(Class<T> type, Provider<T> unscoped);
}
