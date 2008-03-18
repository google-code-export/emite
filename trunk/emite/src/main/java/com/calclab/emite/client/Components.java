package com.calclab.emite.client;

import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.log.Logger;

public interface Components {

	Object get(String componentName);

	Connection getConnection();

	Dispatcher getDispatcher();

	Globals getGlobals();

	Logger getLogger();

	void register(String name, Object component);

	void setConnection(Connection bosh);

	void setDispatcher(Dispatcher dispatcher);

	void setGlobals(Globals globals);

}
