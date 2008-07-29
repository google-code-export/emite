/**
 * 
 */
package com.calclab.emite.widgets.client.base;

public final class DockPoint {
    private final String id;

    public DockPoint(final String id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object obj) {
        return id.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}