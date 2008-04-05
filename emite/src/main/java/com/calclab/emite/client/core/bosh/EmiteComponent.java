package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.dispatcher.DispatcherComponent;

public abstract class EmiteComponent extends DispatcherComponent {
	protected final Emite emite;

	public EmiteComponent(final Emite emite) {
		super(emite.getDispatcher());
		this.emite = emite;
	}

}
