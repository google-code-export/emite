package com.calclab.emite.client.plugin;

import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;

public abstract class SenderPlugin extends PublisherPlugin {
    private final Connection connection;

    public SenderPlugin(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public void attach(final Dispatcher dispatcher) {
        this.when = new FilterBuilder(dispatcher, connection);
        attach();
    }

}
