package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;

public abstract class PublisherPlugin implements Plugin {

    protected FilterBuilder when;

    private Components components;

    public abstract void attach();

    public void attach(final Dispatcher dispatcher) {
        this.when = new FilterBuilder(dispatcher, null);
        attach();
    }

    public abstract void install();

    public void install(final Components components) {
        this.components = components;
        install();
        this.components = null;
    }

    protected Connection dependsOnConnection() {
        return (Connection) getComponent(Components.CONNECTION);
    }

    protected Object getComponent(final String componentName) {
        final Object component = components.get(componentName);
        if (component == null) {
            throw new RuntimeException("Unsatisfied depency to: " + componentName);
        }
        return component;
    }

    protected Dispatcher getDispatcher() {
        return (Dispatcher) getComponent(Components.DISPATCHER);
    }

    protected Globals getGlobals() {
        return (Globals) getComponent(Components.GLOBALS);
    }

    protected void register(final String name, final Object component) {
        components.register(name, component);
    }

}
