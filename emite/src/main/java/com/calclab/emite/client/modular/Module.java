package com.calclab.emite.client.modular;

/**
 * A module is a simple object that installs components inside a container
 * 
 * @author dani
 */
public interface Module {
    public Class<?> getType();

    /**
     * Install the components into the given container
     * 
     * @param container
     *                TODO
     */
    public void onLoad(Container container);
}
