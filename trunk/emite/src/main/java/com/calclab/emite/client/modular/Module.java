package com.calclab.emite.client.modular;

/**
 * A module is a simple object that installs components inside a container
 * @author dani
 */
public interface Module {
    /**
     * Install the components into the given container
     * @param container
     */
    public void onLoad(Container container);
}
