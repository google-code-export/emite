package com.calclab.emite.client.dispatcher;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.plugin.Plugin;

public class DispatcherPlugin implements Plugin {
    public void attach(final Dispatcher dispatcher) {
    }

    public void install(final Components components) {
        final DispatcherDefault dispatcher = new DispatcherDefault(components.getLogger());
        components.setDispatcher(dispatcher);
    }

}
