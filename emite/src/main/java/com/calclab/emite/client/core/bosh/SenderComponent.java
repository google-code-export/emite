package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.dispatcher.DispatcherComponent;

public abstract class SenderComponent extends DispatcherComponent {
	protected final Emite emite;

	public SenderComponent(final Emite emite) {
		super(emite.getDispatcher());
		this.emite = emite;
	}

}
