package com.calclab.suco.client.scopes.context;

public interface Context<O> {
    public void createAll();

    public O getContext();

    public void removeContext(O contextObject);

    public void setContext(O newContext);
}
