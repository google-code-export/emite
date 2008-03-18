package com.calclab.emite.client;

import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.log.Logger;

public interface Components {

	Object get(String componentName);

	IConnection getConnection();

	Dispatcher getDispatcher();

	Globals getGlobals();

	Logger getLogger();

	void register(String name, Object component);

	void setConnection(IConnection bosh);

	void setDispatcher(Dispatcher dispatcher);

	void setGlobals(Globals globals);

}
