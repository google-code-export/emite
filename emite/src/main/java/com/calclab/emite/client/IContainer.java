package com.calclab.emite.client;

import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;

public interface IContainer {

	Object get(String componentName);

	IConnection getConnection();

	IDispatcher getDispatcher();

	IGlobals getGlobals();

	void register(String name, Object component);

	void setConnection(IConnection bosh);

	void setDispatcher(IDispatcher dispatcher);

	void setGlobals(IGlobals globals);

	void setLogger(Logger logger);

}
