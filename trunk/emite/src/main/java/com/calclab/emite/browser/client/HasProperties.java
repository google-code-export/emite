package com.calclab.emite.browser.client;

import java.util.Map;

/**
 * A object that has properties. Used by widgets.
 */
public interface HasProperties {
    public String[] getPropertyNames();

    public void setProperties(Map<String, String> properties);
}
