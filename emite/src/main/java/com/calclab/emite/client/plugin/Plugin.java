package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;

public interface Plugin {
	void install(Components components);

	void start(final FilterBuilder when);
}
