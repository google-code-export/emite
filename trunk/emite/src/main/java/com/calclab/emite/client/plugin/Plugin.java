package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.dispatcher.Dispatcher;

public interface Plugin {
    void attach(final Dispatcher dispatcher);

    void install(Components components);
}
