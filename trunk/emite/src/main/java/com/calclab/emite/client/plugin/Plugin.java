package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;

public interface Plugin {
	void start(final FilterBuilder when, final Components components);
}
