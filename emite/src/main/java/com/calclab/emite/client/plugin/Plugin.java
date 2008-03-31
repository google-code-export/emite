package com.calclab.emite.client.plugin;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.dispatcher.Dispatcher;

public interface Plugin {
    void attach(final Dispatcher dispatcher);

    void install(Container container);
}
