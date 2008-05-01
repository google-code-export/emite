package com.calclab.emite.client.components;

public class AbstractContainer implements Container {
    private final Container delegate;

    public AbstractContainer(final Container delegate) {
	this.delegate = delegate;
    }

    public Component get(final String componentName) {
	return delegate.get(componentName);
    }

    public void install() {
	delegate.install();
    }

    public void install(final String componentName, final Installable startable) {
	delegate.install(componentName, startable);
    }

    public void register(final String componentName, final Component component) {
	delegate.register(componentName, component);
    }

}
