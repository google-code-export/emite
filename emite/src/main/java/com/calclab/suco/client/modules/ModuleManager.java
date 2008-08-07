package com.calclab.suco.client.modules;

import java.util.ArrayList;

import com.calclab.suco.client.container.Container;

public class ModuleManager {

    public static final InstallationStrategy FAIL_IF_PREVIOUSLY_INSTALLED = new InstallationStrategy() {
	public void install(final ModuleManager manager, final Module module) {
	    if (manager.isModuleInstalled(module)) {
		throw new RuntimeException("Module " + module.getType() + " is currently installed.");
	    } else {
		module.onLoad(manager.getContainer());
		manager.setModuleInstalled(module);
	    }
	}
    };

    public static final InstallationStrategy INSTALL_IF_NOT_PREVIOUSLY_INSTALLED = new InstallationStrategy() {
	public void install(final ModuleManager manager, final Module module) {
	    if (!manager.isModuleInstalled(module)) {
		module.onLoad(manager.getContainer());
		manager.setModuleInstalled(module);
	    }
	}
    };

    private final Container container;

    private final ArrayList<Class<? extends Module>> installedModules;

    public ModuleManager(final Container container) {
	this.container = container;
	this.installedModules = new ArrayList<Class<? extends Module>>();
    }

    public Container getContainer() {
	return container;
    }

    public void install(final InstallationStrategy strategy, final Module... modules) {
	for (final Module module : modules) {
	    install(module, strategy);
	}
    }

    /**
     * Install the modules with FAIL_IF_PREVIOUSLY_INSTALLED strategy
     * 
     * @param modules
     */
    public void install(final Module... modules) {
	install(FAIL_IF_PREVIOUSLY_INSTALLED, modules);
    }

    public void install(final Module m, final InstallationStrategy strategy) {
	strategy.install(this, m);
    }

    public boolean isModuleInstalled(final Module module) {
	return installedModules.contains(module.getType());
    }

    public void setModuleInstalled(final Module module) {
	installedModules.add(module.getType());
    }

}
