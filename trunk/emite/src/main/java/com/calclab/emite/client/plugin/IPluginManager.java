package com.calclab.emite.client.plugin;

public interface IPluginManager {
	public void install(String name, Plugin2 plugin);

	public void uninstall(String name);
}
