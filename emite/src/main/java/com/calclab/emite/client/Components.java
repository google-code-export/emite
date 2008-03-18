package com.calclab.emite.client;

import com.calclab.emite.client.action.Dispatcher;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.plugin.PluginManager;

public interface Components {

	Object get(String componentName);

	IConnection getConnection();

	Dispatcher getDispatcher();

	Globals getGlobals();

	Logger getLogger();

	PluginManager getPluginManager();

	void register(String name, Object component);

	void setConnection(IConnection bosh);

	void setDispatcher(Dispatcher dispatcher);

	void setGlobals(Globals globals);

	void setLogger(Logger logger);

	void setPluginManager(PluginManager pluginManager);

}
