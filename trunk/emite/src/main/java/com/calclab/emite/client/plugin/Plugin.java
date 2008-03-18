package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;

public interface Plugin {
	void install(Components components);

	void start(final FilterBuilder when);
}
