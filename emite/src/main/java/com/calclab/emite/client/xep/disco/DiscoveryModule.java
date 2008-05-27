package com.calclab.emite.client.xep.disco;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.ModuleBuilder;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;

/**
 * Implements XEP-0030: Service Discovery
 * 
 * This specification defines an XMPP protocol extension for discovering
 * information about other XMPP entities
 * 
 * @see http://www.xmpp.org/extensions/xep-0030.html
 * 
 * NOT IMPLEMENTED
 * 
 */
public class DiscoveryModule implements Module {

    public Class<?> getType() {
	return DiscoveryModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.registerProvider(DiscoveryManager.class, new Provider<DiscoveryManager>() {
	    public DiscoveryManager get() {
		return new DiscoveryManager(builder.getInstance(Emite.class));
	    }
	}, Scopes.SINGLETON_EAGER);
    }
}
