package com.calclab.modular.client.scopes.context;

public interface Context<O> {
    public void createAll();

    public O getContext();

    public void setContext(O newContext);
}
